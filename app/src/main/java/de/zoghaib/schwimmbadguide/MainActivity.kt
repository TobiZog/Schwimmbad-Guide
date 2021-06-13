package de.zoghaib.schwimmbadguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.zoghaib.schwimmbadguide.adapter.TabAdapter
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.database.DatabasePrePopulator
import de.zoghaib.schwimmbadguide.databinding.ActivityMainBinding
import de.zoghaib.schwimmbadguide.fragments.ListFragment
import de.zoghaib.schwimmbadguide.fragments.MapFragment
import de.zoghaib.schwimmbadguide.fragments.SettingsFragment
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

    private val adapter = TabAdapter(supportFragmentManager, 0)


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


        // TabLayout
        // BottomNavigationView
        binding.vpMain.adapter = adapter
        binding.tlMain.setupWithViewPager(binding.vpMain)

        // Adding the fragments
        adapter.addFragment(MapFragment(pools), resources.getString(R.string.map))
        adapter.addFragment(ListFragment(pools), resources.getString(R.string.list))
        adapter.addFragment(SettingsFragment(), resources.getString(R.string.preferences))
        adapter.notifyDataSetChanged()

        // Setting the icons
        binding.tlMain.getTabAt(0)?.setIcon(R.drawable.ic_map)
        binding.tlMain.getTabAt(1)?.setIcon(R.drawable.ic_view_list)
        binding.tlMain.getTabAt(2)?.setIcon(R.drawable.ic_cog)
    }
}