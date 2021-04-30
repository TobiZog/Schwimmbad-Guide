package de.zoghaib.schwimmbadguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.zoghaib.schwimmbadguide.databinding.ActivityMainBinding

/**
 * First activity on app start
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-01
 */
class MainActivity : AppCompatActivity() {

    /* -------------------- Member Variables -------------------- */

    /** View binding object to access items in the view */
    private lateinit var binding : ActivityMainBinding


    /* -------------------- Lifecycle -------------------- */

    /**
     * Lifecycle method on activity creation
     *
     * @param   savedInstanceState      Save state of the view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // BottomNavigationView
        binding.bnvMain.setOnNavigationItemSelectedListener { item ->

            // Fragment transactor
            val ft = supportFragmentManager.beginTransaction()

            when(item.itemId) {
                R.id.menu_item_map -> {
                    ft.replace(R.id.fgmt_main, MapFragment())
                }

                R.id.menu_item_list -> {
                    ft.replace(R.id.fgmt_main, ListFragment())
                }
            }

            // Send changes
            ft.commit()

            // Result
            true
        }
    }
}