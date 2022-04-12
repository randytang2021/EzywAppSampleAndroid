package com.ezyw.sample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.ezyw.sample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val pluginClass = "com.sweet.plugin.sample.SampleMainActivity"
    private var activityMainBinding: ActivityMainBinding? = null
    private val binding get() = activityMainBinding!!
    private val activityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val resultCode = result.resultCode
        if (resultCode == pluginClass.hashCode()) {
            val intent = result.data
            if (intent != null) {
                val resultFromPlugin = intent.getStringExtra("PLUGIN_DONE")
                Toast.makeText(
                    baseContext,
                    "result from: $resultFromPlugin",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.appContentTv.append("result:${resultFromPlugin}\n")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launchActivityByQualifiedName(pluginClass, baseContext, activityResultLauncher)

    }

}

fun launchActivityByQualifiedName(
    pluginQualifiedName: String,
    context: Context?,
    activityResultLauncher1: ActivityResultLauncher<Intent>
) {
    try {
        Class.forName(pluginQualifiedName)
        val intent = Intent()
        intent.component = ComponentName(
            BuildConfig.APPLICATION_ID,
            pluginQualifiedName
        )
        activityResultLauncher1.launch(intent)
    } catch (e: ClassNotFoundException) {
        Toast.makeText(
            context,
            "please register: $pluginQualifiedName or implement its aar dependency",
            Toast.LENGTH_SHORT
        ).show()
    }
}