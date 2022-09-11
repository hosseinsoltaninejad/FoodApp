package ir.hossein.foodapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.hossein.foodapp.databinding.ActivityMainBinding
import ir.hossein.foodapp.databinding.*
import ir.hossein.foodapp.room.Food
import ir.hossein.foodapp.room.FoodDao
import ir.hossein.foodapp.room.MyDatabase
import kotlin.collections.ArrayList

// implement Room :
// 1. Entity
// 2. Dao
// 3. database

// how to use recycler view :
// 1. create view of recyclerView in activity_main.xml
// 2. create item for recyclerView
// 3. create adapter and view holder for recyclerView
// 4. set adapter to recyclerView and set layout manager

const val BASE_URL_IMAGE = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food"

class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {

    private lateinit var binding: ActivityMainBinding
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var foodDao: FoodDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodDao = MyDatabase.getDatabase(this).foodDao

        val sharedPreferences = getSharedPreferences("foodApp", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }

        showAllData()

        binding.btnRemoveAllFood.setOnClickListener {
            removeAllData()
        }

        binding.btnAddNewFood.setOnClickListener {
            addNewFood()
        }

        binding.edtSearch.addTextChangedListener { editTextInput ->
            searchOnDatabase(editTextInput!!.toString())
        }
    }
    private fun firstRun() {

        val foodList = arrayListOf(
            Food(
                txtSubject = "Hamburger",
                txtPrice = "15",
                txtDistance = "3",
                txtCity = "Isfahan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
                numOfRating = 20,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Grilled fish",
                txtPrice = "20",
                txtDistance = "2.1",
                txtCity = "Tehran, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
                numOfRating = 10,
                rating = 4f
            ),
            Food(
                txtSubject = "Lasania",
                txtPrice = "40",
                txtDistance = "1.4",
                txtCity = "Isfahan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
                numOfRating = 30,
                rating = 2f
            ),
            Food(
                txtSubject = "pizza",
                txtPrice = "10",
                txtDistance = "2.5",
                txtCity = "Zahedan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
                numOfRating = 80,
                rating = 1.5f
            ),
            Food(
                txtSubject = "Sushi",
                txtPrice = "20",
                txtDistance = "3.2",
                txtCity = "Mashhad, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
                numOfRating = 200,
                rating = 3f
            ),
            Food(
                txtSubject = "Roasted Fish",
                txtPrice = "40",
                txtDistance = "3.7",
                txtCity = "Jolfa, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
                numOfRating = 50,
                rating = 3.5f
            ),
            Food(
                txtSubject = "Fried chicken",
                txtPrice = "70",
                txtDistance = "3.5",
                txtCity = "NewYork, USA",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
                numOfRating = 70,
                rating = 2.5f
            ),
            Food(
                txtSubject = "Vegetable salad",
                txtPrice = "12",
                txtDistance = "3.6",
                txtCity = "Berlin, Germany",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
                numOfRating = 40,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Grilled chicken",
                txtPrice = "10",
                txtDistance = "3.7",
                txtCity = "Beijing, China",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
                numOfRating = 15,
                rating = 5f
            ),
            Food(
                txtSubject = "Baryooni",
                txtPrice = "16",
                txtDistance = "10",
                txtCity = "Ilam, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
                numOfRating = 28,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Ghorme Sabzi",
                txtPrice = "11.5",
                txtDistance = "7.5",
                txtCity = "Karaj, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
                numOfRating = 27,
                rating = 5f
            ),
            Food(
                txtSubject = "Rice",
                txtPrice = "12.5",
                txtDistance = "2.4",
                txtCity = "Shiraz, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
                numOfRating = 35,
                rating = 2.5f
            ),
        )
        foodDao.insertAllFoods(foodList)
    }

    private fun showAllData() {

        val foodData = foodDao.getAllFoods()

        foodAdapter = FoodAdapter( ArrayList( foodData ) , this)
        binding.recyclerMain.adapter = foodAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun addNewFood() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.dialogBtnDone.setOnClickListener {

            if (dialogBinding.dialogEdtNameFood.length() > 0 &&
                dialogBinding.dialogEdtFoodCity.length() > 0 &&
                dialogBinding.dialogEdtFoodPrice.length() > 0 &&
                dialogBinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = dialogBinding.dialogEdtNameFood.text.toString()
                val txtCity = dialogBinding.dialogEdtFoodCity.text.toString()
                val txtPrice = dialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = dialogBinding.dialogEdtFoodDistance.text.toString()

                Log.i("TAG", "onCreate: $txtName \n $txtCity \n $txtPrice \n $txtDistance")
                val txtRatingNumber: Int = (1..150).random()
                val ratingBarStar: Float = (1..5).random().toFloat()
//                    val min = 0f
//                    val  max = 5f
//                    val random = min + Random().nextFloat() * ( max - min)
                val randomForUrl = (1 until 12).random()
                val urlPic = BASE_URL_IMAGE + randomForUrl.toString() + ".jpg"

                val newFood = Food(
                    txtSubject = txtName,
                    txtCity = txtCity,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    urlImage = urlPic,
                    numOfRating = txtRatingNumber,
                    rating = ratingBarStar
                )

                foodAdapter.addFood(newFood)
                foodDao.insertFood(newFood)

                binding.recyclerMain.scrollToPosition(0)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "لطفا تمام مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeAllData() {
        foodDao.deleteAllFoods()
        showAllData()
    }

    private fun searchOnDatabase(editTextInput: String) {

        if (editTextInput.isNotEmpty()) {

            // filter data 'h' =>
            val searchData = foodDao.searchFoods(editTextInput)
            foodAdapter.setData( ArrayList( searchData ) )

        } else {
            // show all data =>
            val data = foodDao.getAllFoods()
            foodAdapter.setData( ArrayList( data ) )
        }
    }

    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(updateDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        updateDialogBinding.dialogEdtNameFood.setText(food.txtSubject)
        updateDialogBinding.dialogEdtFoodCity.setText(food.txtCity)
        updateDialogBinding.dialogEdtFoodPrice.setText(food.txtPrice)
        updateDialogBinding.dialogEdtFoodDistance.setText(food.txtDistance)

        updateDialogBinding.dialogBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        updateDialogBinding.dialogBtnUpdateDone.setOnClickListener {

            if (updateDialogBinding.dialogEdtNameFood.length() > 0 &&
                updateDialogBinding.dialogEdtFoodCity.length() > 0 &&
                updateDialogBinding.dialogEdtFoodPrice.length() > 0 &&
                updateDialogBinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = updateDialogBinding.dialogEdtNameFood.text.toString()
                val txtCity = updateDialogBinding.dialogEdtFoodCity.text.toString()
                val txtPrice = updateDialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = updateDialogBinding.dialogEdtFoodDistance.text.toString()

                val newFood = Food(
                    id = food.id ,
                    txtSubject = txtName,
                    txtCity = txtCity,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    urlImage = food.urlImage,
                    numOfRating = food.numOfRating,
                    rating = food.rating
                )

                // update item in adapter =>
                foodAdapter.updateFood(newFood, position)

                // update item in dataBase =>
                foodDao.updateFood( food )

                dialog.dismiss()

            } else {
                Toast.makeText(this, "لطفا تمام مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFoodLongClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogDeleteBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogDeleteBinding.dialogBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogDeleteBinding.dialogBtnSure.setOnClickListener {
            dialog.dismiss()
            // delete item in adapter =>
            foodAdapter.removeFood(food, position)
            // delete item in dataBase =>
            foodDao.deleteFood(food)
        }
    }

}