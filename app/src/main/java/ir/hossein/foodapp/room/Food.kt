package ir.hossein.foodapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_food")
data class Food (

    @PrimaryKey(autoGenerate = true)
    val id : Int? = null ,

    val txtSubject : String ,
    val txtCity: String ,
    val txtPrice : String ,
    val txtDistance : String ,

//    @ColumnInfo(name = "url")
    val urlImage : String ,
    val numOfRating : Int ,
    val rating : Float
        )