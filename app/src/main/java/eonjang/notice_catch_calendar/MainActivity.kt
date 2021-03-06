package eonjang.notice_catch_calendar

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.spec.PSource

var s_color = 0
var s_title = " "
var s_memo = " "
var s_year = 0
var s_month = 0
var s_day = 0
val arrayId = arrayListOf<Int>()


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    var backWait : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
        val arr = simpleDateFormat.format(Date()).split(".")
        s_year = arr[0].toInt()
        s_month = arr[1].toInt()
        s_day = arr[2].toInt()
        Log.e("s_year", s_year.toString())
        Log.e("s_month", s_month.toString())
        Log.e("s_day", s_day.toString())


        dbHelper = DBHelper(this, "NoticeDB.db", null, 1)
        database = dbHelper.writableDatabase
        var mAdapter:ListAdapter=ListAdapter(this)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = mAdapter

        val calendar: CalendarView = findViewById(R.id.calendarView)

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            s_year = year
            s_month = month + 1
            s_day = dayOfMonth
            mAdapter.setList()

            Log.e("year", s_year.toString())
            Log.e("month", s_month.toString())
            Log.e("day", s_day.toString())
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("Type", 1)            //수정, 삭제 = type 1
            intent.putExtra("DB_Id", arrayId[position])
            Log.e("year", arrayId[position].toString())
            startActivity(intent)
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("Type", 2)            //추가 = type 2
            intent.putExtra("Year", s_year)
            intent.putExtra("Month", s_month)
            intent.putExtra("Day", s_day)
            startActivity(intent)
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - backWait >= 2000){
            backWait = System.currentTimeMillis()
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }

    private class ListAdapter(context: Context) : BaseAdapter(){
        private val mContext: Context = context
        private val arrayTitle = arrayListOf<String>()
        private val arrayStime = arrayListOf<String>()
        private val arrayFtime = arrayListOf<String>()
        private val arrayColor = arrayListOf<Int>()

        var dbHelper = DBHelper(context, "NoticeDB.db", null, 1)
        var database = dbHelper.writableDatabase

        init{
            setList()
        }
        override fun getCount(): Int{
            return arrayId.size
        }
        override fun getItemId(position: Int): Long{
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            val selectItem = arrayId.get(position)
            return selectItem
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.memo_list, parent, false)

            val titleView = rowMain.findViewById<TextView>(R.id.list_title)
            titleView.text = arrayTitle.get(position)
            val timeView = rowMain.findViewById<TextView>(R.id.list_time)
            var stime = arrayStime.get(position).split(":").toMutableList()
            var ftime = arrayFtime.get(position).split(":").toMutableList()

            for(i in 0..1){
                if((stime[i].toInt() <10) &&!(stime[i].contains("0"))){
                    stime[i] = "0" + stime[i].toString()
                }else if(stime[i].equals("0")) {
                    stime[i] = "0" + stime[i].toString()
                }

                if((ftime[i].toInt() <10)&&!(ftime[i].contains("0"))){
                    ftime[i] = "0" + ftime[i].toString()
                }else if(ftime[i].equals("0")) {
                    ftime[i] = "0" + ftime[i].toString()
                }
            }
            timeView.text = stime[0]+":"+stime[1]+"~"+ftime[0]+":"+ftime[1]

            //timeView.text = arrayStime.get(position) + "~" + arrayFtime.get(position)
            val colorView = rowMain.findViewById<ImageView>(R.id.list_color)
            colorView.setBackgroundColor(arrayColor.get(position))

            return rowMain
        }
        fun setList(){
            arrayId.clear()
            arrayTitle.clear()
            arrayStime.clear()
            arrayFtime.clear()
            arrayColor.clear()
            var query = "SELECT * FROM calendar WHERE year = '$s_year' AND month = '$s_month' AND day = '$s_day'"
            var c = database.rawQuery(query,null)
            while(c.moveToNext()){
                arrayId.add(c.getInt(c.getColumnIndex("_id")))
                arrayTitle.add(c.getString(c.getColumnIndex("title")))
                arrayStime.add(c.getString(c.getColumnIndex("start_time")))
                arrayFtime.add(c.getString(c.getColumnIndex("finish_time")))
                arrayColor.add(c.getInt(c.getColumnIndex("color")))
            }
            notifyDataSetChanged()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.n1 ->{
                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            }
            R.id.n2 ->{
                var sIntent=Intent(this,NotifiedAllowSetting::class.java)
                startActivity(sIntent)
            }
            R.id.n3 ->{
                var sIntent=Intent(this,KeywordSetting::class.java)
                startActivity(sIntent)
            }
            R.id.n4 ->{
                var sIntent=Intent(this,LogActivity::class.java)
                startActivity(sIntent)
            }
        }
        return true
    }
}
