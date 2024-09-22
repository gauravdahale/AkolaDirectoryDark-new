package gauravdahale.gtech.akoladirectory.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import gauravdahale.gtech.akoladirectory.R

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    textMessage.setText(R.string.bottombar_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    textMessage.setText(R.string.bottombar_profile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_contact_us -> {
                    textMessage.setText(R.string.bottombar_contactus)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
