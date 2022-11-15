package com.example.workoaching.controllers

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.workoaching.R
import com.example.workoaching.utils.ApiManager
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.fragment_creation.*
import kotlinx.android.synthetic.main.fragment_creation.exercisesRecycler
import kotlinx.android.synthetic.main.fragment_creation.progressBarExercisesByCategory
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.workoaching.DAO.DatabaseInfo
import com.example.workoaching.recycleradapter.RecyclerAdapterRoutineExercises
import com.example.workoaching.recycleradapter.RecyclerAdapterSelectExercises
import com.example.workoaching.utils.NetworkConnection
import com.example.workoaching.utils.SQLiteHelper
import kotlinx.android.synthetic.main.fragment_creation.view.*


class CreationFragment : Fragment() {
    var exerciseType=0;

    lateinit var gestureDetector: GestureDetector
    lateinit var validator : AwesomeValidation


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validator = AwesomeValidation(ValidationStyle.BASIC)

        validator.addValidation(activity,
            R.id.routineName, RegexTemplate.NOT_EMPTY,
            R.string.empty_field
        )

        routineName.setText(RecyclerAdapterSelectExercises.routineName.toString())
        changeExerciseTypeName(exerciseType)
        nextExerciseType.setOnClickListener{
            if(validator.validate()){
                RecyclerAdapterSelectExercises.routineName= routineName.text.toString()
                nextExercise()
            }

        }

        prevExerciseType.setOnClickListener{
            prevExercise()
        }
        stepView.state.animationType(StepView.ANIMATION_ALL).animationDuration(R.integer.material_motion_duration_short_1).stepsNumber(6).commit()


       // ApiManager.getExercisesByCategory(
       //     exercisesRecycler,
       //     GridLayoutManager(activity, 2), progressBarExercisesByCategory, exerciseType, stepView
       // )


        prevExerciseType.visibility=View.GONE



        //Toast.makeText(view.context,stepView.currentStep.toString(),Toast.LENGTH_SHORT).show()

    }
     fun prevExercise(){
        exerciseType -= 1
        nextExerciseType.visibility=View.VISIBLE
        if(exerciseType==0){
            prevExerciseType.visibility=View.GONE
            routineNameContainer.visibility = View.VISIBLE
            exercisesRecycler.visibility=View.GONE
        }

         changeExerciseTypeName(exerciseType)
        //Toast.makeText(view.context,stepView.currentStep.toString(),Toast.LENGTH_SHORT).show()
        progressBarExercisesByCategory.visibility=View.VISIBLE



        activity?.let { if(NetworkConnection(it.applicationContext).isOnline()){

            ApiManager.getExercisesByCategory(
                exercisesRecycler,
                GridLayoutManager(activity, 2),
                progressBarExercisesByCategory,
                exerciseType,
                stepView
            )

        }else{

           DatabaseInfo.db.getExercisesByCategory(    exercisesRecycler,   GridLayoutManager(activity, 2),
                progressBarExercisesByCategory,exerciseType,  stepView)
        }
        }







    }
     fun nextExercise(){
         exerciseType += 1

         if(exerciseType>0){
             routineNameContainer.visibility=View.GONE
             exercisesRecycler.visibility=View.VISIBLE
         }

        if(exerciseType<=5){

            prevExerciseType.visibility=View.VISIBLE

            changeExerciseTypeName(exerciseType)

            //Toast.makeText(view.context,stepView.currentStep.toString(),Toast.LENGTH_SHORT).show()

            progressBarExercisesByCategory.visibility=View.VISIBLE




            activity?.let { if(NetworkConnection(it.applicationContext).isOnline()){

                ApiManager.getExercisesByCategory(
                    exercisesRecycler,
                    GridLayoutManager(activity, 2),
                    progressBarExercisesByCategory,
                    exerciseType,
                    stepView
                )

            }else{
                try {
                    DatabaseInfo.db.getExercisesByCategory(    exercisesRecycler,   GridLayoutManager(activity, 2),
                        progressBarExercisesByCategory,exerciseType,  stepView)
                }catch (e : Exception){
                    Toast.makeText(view?.context,"Fail",Toast.LENGTH_SHORT).show()
                }

            }
            }



        }else if(exerciseType>5){
            exerciseType-=1
            if(RecyclerAdapterSelectExercises.getSelectedExercises().size > 0){

            val createRoutineIntent= Intent(view?.context,CreateRoutineActivity::class.java )
            createRoutineIntent.putExtra("routineName",routineName.text.toString())
            view?.context!!.startActivity(createRoutineIntent)
            }else{
                Toast.makeText(view?.context,R.string.select_one_exercise,Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun changeExerciseTypeName(type : Int){

         when(type){
             0->{

                 exerciseTypeName.visibility=View.GONE
                 exerciseTypeImage.visibility=View.GONE
             }
             1->{

                 exerciseTypeName.setText(R.string.chest_exercises)
                 exerciseTypeImage.setBackgroundResource(R.drawable.pecho_category)
             }
             2->{


                 exerciseTypeName.setText(R.string.shoulder_exercises)
                 exerciseTypeImage.setBackgroundResource(R.drawable.hombro_category)
             }
             3->{

                 exerciseTypeName.setText(R.string.back_exercises)
                 exerciseTypeImage.setBackgroundResource(R.drawable.espalda_cateogry)
             }
             4->{

                 exerciseTypeName.setText(R.string.arm_exercises)
                     exerciseTypeImage.setBackgroundResource(R.drawable.brazo_category)
             }
             5->{

                 exerciseTypeName.setText(R.string.abs_exercises)
                 exerciseTypeImage.setBackgroundResource(R.drawable.abdomen_category)
             }
         }


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_creation, container, false)

    }





}