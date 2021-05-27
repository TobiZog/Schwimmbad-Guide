package de.zoghaib.schwimmbadguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.database.DatabasePrePopulator
import de.zoghaib.schwimmbadguide.databinding.ActivityMainBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool

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

    /** Database handler object */
    private lateinit var dbHandler : DatabaseHandler

    /** Array list with all pools */
    private val pools = ArrayList<SwimmingPool>()


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


        // Pre populate the database with the CSV datas
        val databasePrePopulator = DatabasePrePopulator(this)
        databasePrePopulator.initDatabase()


        // Initialize the database handler
        dbHandler = DatabaseHandler(this)


        // Load the pools
        val rawDatasets = dbHandler.readTableToArrayList("POOLS")

        if (rawDatasets != null) {
            for(pool in rawDatasets) {
                pools.add(
                    SwimmingPool(this, pool.getAsInteger("Id"))
                )
            }
        }


        // BottomNavigationView
        binding.bnvMain.setOnNavigationItemSelectedListener { item ->

            // Fragment transactor
            val ft = supportFragmentManager.beginTransaction()

            when(item.itemId) {
                R.id.menu_item_map -> {
                    ft.replace(R.id.fgmt_main, MapFragment(pools))
                }

                R.id.menu_item_list -> {
                    ft.replace(R.id.fgmt_main, ListFragment(pools))
                }
            }

            // Send changes
            ft.commit()

            // Result
            true
        }
    }
}