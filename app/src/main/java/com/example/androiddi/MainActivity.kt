package com.example.androiddi

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddi.databinding.ActivityMainBinding
import com.example.androiddi.forRecyclerView.CustomRecyclerAdapterForExams
import com.example.androiddi.forRecyclerView.RecyclerItemClickListener
import com.example.androiddi.directions.Direction
import com.example.androiddi.directions.DirectionOperator
import com.example.androiddi.directions.Tour
import com.example.androiddi.directions.dbWithRoom.App
import com.example.androiddi.directions.dbWithRoom.AppDatabase
import com.example.androiddi.directions.dbWithRoom.DirectionOperatorDao
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TourDetailsDialogFragment.OnInputListenerSortId
{
    private val gsonBuilder = GsonBuilder()
    private val gson: Gson = gsonBuilder.create()
    private val serverIP = "192.168.1.69"
    private val serverPort = 8998
    private lateinit var connection: Connection
    private var connectionStage: Int = 0
    private var startTime: Long = 0

    private lateinit var db: AppDatabase
    private lateinit var doDao: DirectionOperatorDao

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nv: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerViewTours: RecyclerView

    private var diro: DirectionOperator = DirectionOperator()
    private var currentDirectionID: Int = -1
    private var currentTourID: Int = -1
    private var waitingForUpdate: Boolean = false
    private lateinit var roundTitle: String

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        nv = binding.navView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply { setNavigationIcon(R.drawable.ic_my_menu) }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        progressBar = findViewById(R.id.progressBar)
        recyclerViewTours = findViewById(R.id.recyclerViewExams)
        recyclerViewTours.visibility = View.INVISIBLE
        recyclerViewTours.layoutManager = LinearLayoutManager(this)

        recyclerViewTours.addOnItemTouchListener(
            RecyclerItemClickListener(
                recyclerViewTours,
                object : RecyclerItemClickListener.OnItemClickListener
                {
                    override fun onItemClick(view: View, position: Int)
                    {
                        currentTourID = position
                        val toast = Toast.makeText(
                            applicationContext,
                            "Стоимость: ${diro.getTour(currentDirectionID, currentTourID)
                                .cost} руб.",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    override fun onItemLongClick(view: View, position: Int)
                    {
                        currentTourID = position
                        val examDetails = TourDetailsDialogFragment()
                        val tempExam = diro.getTour(currentDirectionID, currentTourID)
                        val bundle = Bundle()
                        bundle.putString("country", tempExam.country)
                        bundle.putString("duration", tempExam.duration)
                        bundle.putString("rate", tempExam.rate.toString())
                        bundle.putString("startDate", tempExam.startDate)
                        bundle.putString("endDate", tempExam.endDate)
                        bundle.putString("cost", tempExam.cost.toString())
                        bundle.putString("isAvailable", tempExam.isAvailable.toString())
                        bundle.putString("comment", tempExam.comment)
                        bundle.putString("connection", connectionStage.toString())
                        examDetails.arguments = bundle
                        examDetails.show(fragmentManager, "MyCustomDialog")
                    }
                }
            )
        )

        db = App.instance?.database!!
        doDao = db.groupOperatorDao()
        startTime = System.currentTimeMillis()
        connection = Connection(serverIP, serverPort, "REFRESH", this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean
    {
        if (currentDirectionID != -1 && connectionStage == 1)
        {
            menu.getItem(0).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val id = item.itemId
        if (id == R.id.action_add)
        {
            val intent = Intent()
            intent.setClass(this, EditTourActivity::class.java)
            intent.putExtra("action", 1)
            startActivityForResult(intent, 1)
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class Connection(
        private val SERVER_IP: String,
        private val SERVER_PORT: Int,
        private val refreshCommand: String,
        private val activity: Activity
    ) {
        private var outputServer: PrintWriter? = null
        private var inputServer: BufferedReader? = null
        var thread1: Thread? = null
        private var threadT: Thread? = null

        internal inner class Thread1Server : Runnable {
            override fun run()
            {
                val socket: Socket
                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    outputServer = PrintWriter(socket.getOutputStream())
                    inputServer = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Thread(Thread2Server()).start()
                    sendDataToServer(refreshCommand)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class Thread2Server : Runnable {
            override fun run() {
                while (true) {
                    try {
                        val message = inputServer!!.readLine()
                        if (message != null)
                        {
                            activity.runOnUiThread { processingInputStream(message) }
                        } else {
                            thread1 = Thread(Thread1Server())
                            thread1!!.start()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        internal inner class Thread3Server(private val message: String) : Runnable
        {
            override fun run()
            {
                outputServer!!.write(message)
                outputServer!!.flush()
            }
        }

        internal inner class ThreadT : Runnable
        {
            override fun run() {
                while (true)
                {
                    if (System.currentTimeMillis() - startTime > 5000L && connectionStage == 0)
                    {
                        activity.runOnUiThread { Snackbar.make(findViewById(R.id.app_bar_main),
                            "Подключиться не удалось!\n" +
                                    "Будет использоваться локальная база данных.",
                            Snackbar.LENGTH_LONG)
                            .show() }
                        connectionStage = -1
                        activity.runOnUiThread { progressBar.visibility = View.INVISIBLE }
                        diro = doDao.getById(1)
                        for (i in 0 until diro.getDirections().size)
                        {
                            activity.runOnUiThread { nv.menu.add(0, i, 0,
                                diro.getDirections()[i].name as CharSequence) }
                        }
                    }
                }
            }
        }

        fun sendDataToServer(text: String)
        {
            Thread(Thread3Server(text + "\n")).start()
        }

        private fun processingInputStream(text: String)
        {
            doDao.delete(DirectionOperator())
            val tempDo: DirectionOperator = gson.fromJson(text, DirectionOperator::class.java)
            doDao.insert(tempDo)

            if (connectionStage != 1)
            {
                Snackbar.make(findViewById(R.id.app_bar_main),
                    "Успешно подключено!\n" +
                            "Будут использоваться серверные данные.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }

            progressBar.visibility = View.INVISIBLE
            for (i in 0 until diro.getDirections().size)
            {
                nv.menu.removeItem(i)
            }
            val tempArrayListDirections: ArrayList<Direction> = tempDo.getDirections()
            diro.setDirections(tempArrayListDirections)
            for (i in 0 until tempArrayListDirections.size)
            {
                nv.menu.add(
                    0, i, 0,
                    tempArrayListDirections[i].name as CharSequence
                )
            }
            if (waitingForUpdate || connectionStage == -1)
            {
                waitingForUpdate = false
                if (currentDirectionID != -1)
                {
                    recyclerViewTours.adapter = CustomRecyclerAdapterForExams(
                        diro.getCountries(currentDirectionID),
                        diro.getRates(currentDirectionID),
                        diro.getDurations(currentDirectionID)
                    )
                }
            }
            connectionStage = 1
        }

        init {
            thread1 = Thread(Thread1Server())
            thread1!!.start()
            threadT = Thread(ThreadT())
            threadT!!.start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Snackbar.make(findViewById(R.id.app_bar_main),
            "Выбран: ${item.title}.",
            Snackbar.LENGTH_LONG)
            .show()
        drawerLayout.closeDrawer(GravityCompat.START)
        roundTitle = "${item.title}"
        toolbar.title = roundTitle
        invalidateOptionsMenu()
        currentDirectionID = item.itemId
        recyclerViewTours.adapter = CustomRecyclerAdapterForExams(
            diro.getCountries(currentDirectionID),
            diro.getRates(currentDirectionID),
            diro.getDurations(currentDirectionID))
        recyclerViewTours.visibility = View.VISIBLE
        return true
    }

    fun delTour()
    {
        connection.sendDataToServer("d$currentDirectionID,$currentTourID")
        waitingForUpdate = true
    }

    override fun sendInputSortId(sortId: Int)
    {
        if (sortId > -1 && sortId < 8)      // Сортировка
        {
            diro.sortTours(currentDirectionID, sortId)
            if (connectionStage == 1)
            {
                connection.sendDataToServer("u" + gson.toJson(diro))
            }
            toolbar.title = when (sortId)
            {
                0 -> "$roundTitle сорт. Страна"
                1 -> "$roundTitle сорт. Длительность"
                2 -> "$roundTitle сорт. ★ отеля"
                3 -> "$roundTitle сорт. Д. начала"
                4 -> "$roundTitle сорт. Д. конца"
                5 -> "$roundTitle сорт. Цена"
                6 -> "$roundTitle сорт. Доступен"
                7 -> "$roundTitle сорт. Описание"
                else -> roundTitle
            }
        }
        if (sortId == 8)        // Удаление
        {
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragmentDelTour = MyDialogFragmentDelTour()
            val bundle = Bundle()
            bundle.putString("name", diro.getTour(currentDirectionID, currentTourID).country)
            myDialogFragmentDelTour.arguments = bundle
            myDialogFragmentDelTour.show(manager, "myDialog")
        }
        if (sortId == 9)        // Изменение
        {
            val tempTask = diro.getTour(currentDirectionID, currentTourID)
            val intent = Intent()
            intent.setClass(this, EditTourActivity::class.java)
            intent.putExtra("action", 2)
            intent.putExtra("country", tempTask.country)
            intent.putExtra("duration", tempTask.duration)
            intent.putExtra("rate", tempTask.rate.toString())
            intent.putExtra("startDate", tempTask.startDate)
            intent.putExtra("endDate", tempTask.endDate)
            intent.putExtra("cost", tempTask.cost.toString())
            intent.putExtra("isAvailable", tempTask.isAvailable.toString())
            intent.putExtra("comment", tempTask.comment)
            startActivityForResult(intent, 1)
        }
        recyclerViewTours.adapter = CustomRecyclerAdapterForExams(
            diro.getCountries(currentDirectionID),
            diro.getRates(currentDirectionID),
            diro.getDurations(currentDirectionID))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            val action = data?.getSerializableExtra("action") as Int
            val examName = data.getSerializableExtra("country") as String
            val teacherName = data.getSerializableExtra("duration") as String
            val auditory = data.getSerializableExtra("rate") as Int
            val date = data.getSerializableExtra("startDate") as String
            val time = data.getSerializableExtra("endDate") as String
            val people = data.getSerializableExtra("cost") as Int
            val abstract = data.getSerializableExtra("isAvailable") as Int
            val comment = data.getSerializableExtra("comment") as String
            val tempTour = Tour(examName, teacherName, auditory, date, time, people
                , abstract, comment)
            val tempTaskJSON: String = gson.toJson(tempTour)

            if (action == 1)
            {
                val tempStringToSend = "a${diro.getDirections()[currentDirectionID].name}##$tempTaskJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
            if (action == 2)
            {
                val tempStringToSend = "e$currentDirectionID,$currentTourID##$tempTaskJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
        }
    }
}