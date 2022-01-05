package com.nicovenot.todo.tasklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.nicovenot.todo.*
import com.nicovenot.todo.databinding.FragmentTaskListBinding
import com.nicovenot.todo.network.Api
import com.nicovenot.todo.model.Task
import com.nicovenot.todo.form.FormActivity
import com.nicovenot.todo.viewModel.TaskListViewModel
import com.nicovenot.todo.tasklist.TaskListAdapter
import kotlinx.coroutines.launch


class TaskListFragment : Fragment() {

    private val myAdapter = TaskListAdapter();
    private val viewModel = TaskListViewModel();

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            val oldTask = viewModel.taskList.value?.firstOrNull {it.id == task.id}
            if (oldTask != null) {
                viewModel.updateTask(task, oldTask);
            } else {
                viewModel.createTask(task);
            }
            myAdapter.submitList(viewModel.taskList.value?.toList());
        }
    }

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            myAdapter.submitList(newList);
        }

        val recyclerView = binding.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity);
        recyclerView.adapter = myAdapter

        viewModel.refresh();

        val button = binding.floatingActionButton;
        button.setOnClickListener {
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        // Delete
        myAdapter.onCLickDelete = { task ->
            viewModel.deleteTask(task);
        }

        // Edit
        myAdapter.onClickEdit = { task ->
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("taskToEdit", task)
            formLauncher.launch(intent)
        }

        binding.ImageView.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            formLauncher.launch(intent)
        }

        binding.deco.setOnClickListener {
            val editor = activity!!.getSharedPreferences("TOKEN_SHARE", Context.MODE_PRIVATE).edit()
            editor.putString("TOKEN", "null")
            editor.apply()
            val intent = Intent(activity, AuthenticationActivity::class.java)
            formLauncher.launch(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        if (Api.TOKEN == "null") {
            val intent = Intent(activity, AuthenticationActivity::class.java)
            formLauncher.launch(intent)
            return;
        }
        binding.ImageView.load("https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png") {
            transformations(CircleCropTransformation())
        }
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            val userInfoTextView = binding.userInfoTextView;
            userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
            var img = if (userInfo.avatar.toString() != "null") {
                userInfo.avatar.toString();
            } else {
                "https://preview.redd.it/mdcpue3kp3811.jpg?auto=webp&s=0f094bfc4e30cd38a01c2fe07ead51d6b3fce0f2"
            }
            binding.ImageView.load(img) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_launcher_background)
            }
        }
    }
}