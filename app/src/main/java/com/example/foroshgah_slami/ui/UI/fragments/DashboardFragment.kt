package com.example.foroshgah_slami.ui.UI.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.FragmentDashboardBinding
import com.example.foroshgah_slami.ui.UI.activities.SettingsActivity

class DashboardFragment : Fragment() {

private var _binding: FragmentDashboardBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setHasOptionsMenu(true)
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = FragmentDashboardBinding.inflate(inflater, container, false)
    val root: View = binding.root
    val textView: TextView = binding.textDashboard

      textView.text = "DashboardFragment"

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.dashboard_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId

    when (id) {

      R.id.action_settings -> {
        startActivity(Intent(activity, SettingsActivity::class.java))
        return true

      }
    }
    return super.onOptionsItemSelected(item)
  }
}