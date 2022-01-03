package com.nicovenot.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.nicovenot.todo.R
import com.nicovenot.todo.model.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val taskToEdit = intent.getSerializableExtra("taskToEdit") as? Task;
        if (taskToEdit != null) {
            findViewById<EditText>(R.id.editTitle).setText(taskToEdit.title)
            findViewById<EditText>(R.id.editText).setText(taskToEdit.description)
        }

        val buttonInsert = findViewById<ImageButton>(R.id.buttonInsert);
        buttonInsert.setOnClickListener {
            val title = findViewById<EditText>(R.id.editTitle).text.toString();
            val desc = findViewById<EditText>(R.id.editText).text.toString();
            val newTask = Task(taskToEdit?.id ?: UUID.randomUUID().toString(), title, desc);
            intent.putExtra("task", newTask);
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}