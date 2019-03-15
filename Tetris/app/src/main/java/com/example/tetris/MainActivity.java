package com.example.tetris;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends AppCompatActivity {

    int square1Pos;
    int square2Pos;
    int square3Pos;
    int square4Pos;

    int rowCount;
    int columnCount;

    int gameMatrix[];
    int pieceNumber;
    int pieceDrawableId;
    boolean pieceOnGame;
    boolean gameOver;
    int pieceRotationNumber;
    int gameSpeed;
    Semaphore semaphore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameOver = false;
        this.semaphore = new Semaphore(1);
        GridLayout grid = findViewById(R.id.gridLayout);
        this.rowCount = grid.getRowCount();
        this.columnCount = grid.getColumnCount();
        this.gameMatrix = new int[this.rowCount * this.columnCount];

        this.square1Pos = 0;
        this.square2Pos = 0;
        this.square3Pos = 0;
        this.square4Pos = 0;
        this.pieceNumber = 0;
        this.pieceOnGame = false;
        this.pieceRotationNumber = 1;

        this.gameSpeed = 1000;
        this.initGridLayout();
        this.initGridMarginUI();
       // Log.i("matrix", this.gameMatrixToString());

        // Runnable
        final Handler handler = new Handler();
        Runnable r = new Runnable() {

            public void run() {
                //Log.i("ches", "ches");

                if (pieceOnGame) {

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (ValidateDownMovement()) {

                        LowerPieceUI();
                        semaphore.release();

                    } else {


                        pieceOnGame = false;
                        try {
                            CheckCompleteRow();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        semaphore.release();

                    }

                }
                else {
                    // Spawn piece
                    SpawnPiece();
                    pieceOnGame = true;
                }

                handler.postDelayed(this, gameSpeed);
            }
        };
        handler.postDelayed(r, this.gameSpeed);

        // End Runnable

    }



    public void GameOver(){

        this.gameOver = true;
        Toast toast = Toast.makeText(this,"Fin del Juego",Toast.LENGTH_LONG);
        toast.show();

    }


    public void SpawnPiece() {

        // Gen randon number

        if(this.gameOver){
            return;
        }

        Random ran = new Random();
        int pieceToGenerate = ran.nextInt(7) + 1;

        //This works
        this.pieceNumber = pieceToGenerate;
        this.pieceRotationNumber = 1;

        this.pieceNumber = 6; // TEST, THIS MUST BE ERASED!!!!!!!!!
        pieceToGenerate = 6; // MUST BE ERASED

        if (pieceNumber == 1) {

            if(! this.PlacePiece(14, 15, 24, 25, pieceToGenerate)){

                this.GameOver();
                return;
            }
            this.PlacePieceUi(14, 15, 24, 25, R.drawable.yellowcelltest);


        } else if (pieceNumber == 2) {

            if( !this.PlacePiece(14, 24, 34, 44, pieceToGenerate)){
                this.GameOver();
                return;
            }
            this.PlacePieceUi(14, 24, 34, 44, R.drawable.bluecelltest);

        } else if (pieceNumber == 3) {

            this.PlacePieceUi(14, 15, 23, 24, R.drawable.redcelltest);
            this.PlacePiece(14, 15, 23, 24, pieceToGenerate);

        } else if (pieceNumber == 4) {
            if(!this.PlacePiece(13, 14, 24, 25, pieceToGenerate)){

                this.GameOver();
                return;
            }
            this.PlacePieceUi(13, 14, 24, 25, R.drawable.greencelltest);

        } else if (pieceNumber == 5) {


            if(!this.PlacePiece(14, 24, 34, 35, pieceToGenerate)){
                this.GameOver();
                return;
            }
            this.PlacePieceUi(14, 24, 34, 35, R.drawable.orangecelltest);

        } else if (pieceNumber == 6) {

            if(!this.PlacePiece(14, 24, 33, 34, pieceToGenerate)){
                this.GameOver();
                return;
            }
            this.PlacePieceUi(14, 24, 33, 34, R.drawable.pinkcelltest);


        } else if (pieceNumber == 7) {

            if(!this.PlacePiece(13, 14, 15, 24, pieceToGenerate)){
                this.GameOver();
                return;
            }
            this.PlacePieceUi(13, 14, 15, 24, R.drawable.purplecelltest);
        }


    }

    public String gameMatrixToString() {

        GridLayout grid = findViewById(R.id.gridLayout);
        int count = grid.getColumnCount() * grid.getRowCount();
        String result = "";
        int number = 0;
        for (int i = 0; i < count; i++) {

            number = this.gameMatrix[i];
            result += String.valueOf(number) + " -> ";


        }
        return result;
    }


    public void ClearActualPieces() {

        GridLayout grid = findViewById(R.id.gridLayout);
        // Clear actual pieces
        ImageView imageView1, imageView2, imageView3, imageView4;
        imageView1 = (ImageView) grid.getChildAt(this.square1Pos);
        imageView2 = (ImageView) grid.getChildAt(this.square2Pos);
        imageView3 = (ImageView) grid.getChildAt(this.square3Pos);
        imageView4 = (ImageView) grid.getChildAt(this.square4Pos);

        imageView1.setImageResource(android.R.color.transparent);
        imageView2.setImageResource(android.R.color.transparent);
        imageView3.setImageResource(android.R.color.transparent);
        imageView4.setImageResource(android.R.color.transparent);

        imageView1.setBackground(null);
        imageView2.setBackground(null);
        imageView3.setBackground(null);
        imageView4.setBackground(null);

        imageView1.setImageDrawable(null);
        imageView2.setImageDrawable(null);
        imageView3.setImageDrawable(null);
        imageView4.setImageDrawable(null);



        this.gameMatrix[this.square1Pos] = 0;
        this.gameMatrix[this.square2Pos] = 0;
        this.gameMatrix[this.square3Pos] = 0;
        this.gameMatrix[this.square4Pos] = 0;
    }


    public void MoveLeft() {


        this.square1Pos--;
        this.square2Pos--;
        this.square4Pos--;
        this.square3Pos--;

    }

    public void MoveLeftUI() {

        this.ClearActualPieces();
        MoveLeft();
        this.UpdateUi();


    }

    public void MoveRight() {

        this.square1Pos++;
        this.square2Pos++;
        this.square3Pos++;
        this.square4Pos++;

    }

    public void MoveRightUI() {

        this.ClearActualPieces();
        MoveRight();
        this.UpdateUi();
    }



    public void Rotate(){

        if(this.pieceRotationNumber == 1){

            if(this.pieceNumber == 2){

                this.square1Pos = this.square1Pos + columnCount + 1;
                this.square3Pos = this.square3Pos - columnCount - 1;
                this.square4Pos = this.square4Pos - (columnCount *2 ) - 2;

            }
            else if(this.pieceNumber == 3){

                this.square2Pos = this.square4Pos;
                this.square3Pos = this.square3Pos - (columnCount * 2);
                this.square4Pos = this.square4Pos - columnCount - 1;

            }
            else if(this.pieceNumber == 4){

                this.square3Pos = this.square1Pos;
                this.square1Pos = this.square1Pos - columnCount + 1;
                this.square4Pos = this.square4Pos - 2;

            }
            else if (this.pieceNumber == 5){

                this.square1Pos = this.square2Pos + 1;
                this.square3Pos = this.square2Pos - 1;
                this.square4Pos = this.square4Pos - 2;

            }
            else if(this.pieceNumber == 6){

                this.square1Pos = this.square2Pos + 1;
                this.square4Pos = this.square2Pos - 1;
                this.square3Pos = this.square3Pos - (2* columnCount);
            }
            else if(this.pieceNumber == 7){

                this.square1Pos = this.square2Pos - columnCount;
                this.square3Pos = this.square2Pos  + columnCount;
                this.square4Pos = this.square2Pos - 1;
            }
        }


        else if(this.pieceRotationNumber == 2){

            if(this.pieceNumber == 2){

                this.square1Pos = this.square2Pos - columnCount;
                this.square3Pos = this.square2Pos + columnCount;
                this.square4Pos = this.square2Pos + (2 * columnCount);
                this.pieceRotationNumber = 1;

            }
            else if(this.pieceNumber == 3){

                this.square2Pos = this.square1Pos + 1;
                this.square3Pos = this.square1Pos +columnCount - 1;
                this.square4Pos = this.square1Pos + columnCount;

            }
            else if(this.pieceNumber == 4){

                this.square3Pos = this.square2Pos + columnCount;
                this.square4Pos = this.square2Pos + columnCount + 1;
                this.square1Pos = this.square2Pos - 1;
            }
            else if(this.pieceNumber == 5){

                this.square1Pos = this.square2Pos + columnCount;
                this.square3Pos = this.square2Pos - columnCount;
                this.square4Pos = this.square2Pos - columnCount - 1;

            }
            else if(this.pieceNumber == 6){

                this.square4Pos = this.square2Pos - columnCount;
                this.square3Pos = this.square2Pos - columnCount + 1;
                this.square1Pos = this.square2Pos + columnCount;

            }
            else if(this.pieceNumber == 7) {

                this.square1Pos = this.square2Pos + 1;
                this.square4Pos = this.square2Pos - columnCount;
                this.square3Pos = this.square2Pos - 1;
            }
        }
        else if(this.pieceRotationNumber == 3){

            if(this.pieceNumber == 2){

                this.pieceRotationNumber = 1;
                this.Rotate();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 1;
                this.Rotate();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 1;
                this.Rotate();

            }
            else if(this.pieceNumber == 5){

                this.square1Pos = this.square2Pos - 1;
                this.square3Pos = this.square2Pos + 1;
                this.square4Pos = this.square2Pos - columnCount + 1;

            }
            else if(this.pieceNumber == 6){

                this.square1Pos = this.square2Pos - 1;
                this.square4Pos = this.square2Pos + 1;
                this.square3Pos = this.square2Pos + columnCount + 1;

            }
            else if(this.pieceNumber == 7) {

                this.square1Pos = this.square2Pos + columnCount;
                this.square3Pos = this.square2Pos - columnCount;
                this.square4Pos = this.square2Pos + 1;
            }

        }
        else if(this.pieceRotationNumber == 4){

            if(this.pieceNumber == 2){

                this.pieceRotationNumber = 2;
                this.Rotate();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 1;
                this.Rotate();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 1;
                this.Rotate();

            }
            else if(this.pieceNumber == 5){

                this.square1Pos = this.square2Pos - columnCount;
                this.square3Pos = this.square2Pos + columnCount;
                this.square4Pos = this.square2Pos + columnCount + 1;

            }
            else if(this.pieceNumber == 6){

                this.square1Pos = this.square2Pos - columnCount;
                this.square4Pos = this.square2Pos + columnCount;
                this.square3Pos = this.square2Pos + columnCount - 1;

            }
            else if(this.pieceNumber == 7) {

                this.square1Pos = this.square2Pos - 1;
                this.square3Pos = this.square2Pos + 1;
                this.square4Pos = this.square2Pos + columnCount;

            }

        }

        // after all ifs
        this.pieceRotationNumber++;
        if(this.pieceRotationNumber == 5){
            this.pieceRotationNumber = 1;
        }

    }
    public void RotateUI(){

        this.ClearActualPieces();
        this.Rotate();
        this.UpdateUi();

    }

    public boolean ValidateRotationMovement(){

        if(this.pieceRotationNumber == 1){

            if(this.pieceNumber == 2) {

                int nextPos1, nextPos3, nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount + 1];
                nextPos3 = this.gameMatrix[this.square3Pos - columnCount - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - (columnCount *2 ) - 2];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 3) {

                int nextPos3,nextPos4;
                nextPos3 = this.gameMatrix[this.square3Pos - (columnCount *2)];
                nextPos4 = this.gameMatrix[this.square4Pos - columnCount - 1];
                if(nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;


            }
            else if(this.pieceNumber == 4){

                int nextPos1, nextPos3, nextPos4;

                nextPos1 = this.gameMatrix[this.square1Pos - columnCount + 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 2];

                if(nextPos1 == 0  && nextPos4 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 5){

                int nextPos1, nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos + 1 ];
                nextPos3 = this.gameMatrix[this.square2Pos - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 2];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 6){

                int nextPos1, nextPos3, nextPos4;

                nextPos1 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square2Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - (2* columnCount)];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 7){

                int nextPos1;
                nextPos1 = this.gameMatrix[this.square2Pos - columnCount];

                if(nextPos1 == 0) {
                    return true;
                }
                return false;
            }
        }

        else if(this.pieceRotationNumber == 2){

            if(this.pieceNumber == 2){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos - columnCount];
                nextPos3 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square2Pos + (2* columnCount)];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 ==0 ){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 3){

                int nextPos3,nextPos2;
                nextPos2 = this.gameMatrix[this.square1Pos + 1];
                nextPos3 = this.gameMatrix[this.square1Pos + columnCount - 1];
                if(nextPos3 == 0 && nextPos2 == 0){
                    return true;
                }
                return false;
            }

            else if(this.pieceNumber == 4){

                int nextPos3,nextPos4;
                nextPos3 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square2Pos + columnCount + 1];
                if(nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;

            }

            else if(this.pieceNumber == 5){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos3 = this.gameMatrix[this.square2Pos - columnCount];
                nextPos4 = this.gameMatrix[this.square2Pos - columnCount - 1];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0 ){
                    return true;
                }
                return false;
            }

            else if(this.pieceNumber == 6){

                int nextPos1,nextPos3,np4;

                np4 = this.gameMatrix[this.square2Pos - columnCount];
                nextPos3 = this.gameMatrix[this.square2Pos - columnCount + 1];
                nextPos1= this.gameMatrix[this.square2Pos + columnCount];

                if(nextPos1 == 0 && nextPos3 == 0 && np4 ==0){
                    return true;
                }


            }
            else if(this.pieceNumber == 7){


                int nextPos1 = this.gameMatrix[this.square2Pos + 1];
                if(nextPos1 == 0){
                    return true;
                }
                return false;

            }

        }
        else if(this.pieceRotationNumber == 3){

            if(this.pieceNumber == 2){

                this.pieceRotationNumber = 1;
                return this.ValidateRotationMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 1;
                return this.ValidateRotationMovement();
            }

            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 1;
                return this.ValidateRotationMovement();
            }
            else if(this.pieceNumber == 5){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos - 1];
                nextPos3 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square2Pos - columnCount + 1];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }

            else if(this.pieceNumber == 6){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos - 1];
                nextPos4 = this.gameMatrix[this.square2Pos + 1];
                nextPos3 = this.gameMatrix[this.square2Pos + columnCount + 1];
                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0 ){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 7){

                int nextPos1 = this.gameMatrix[this.square2Pos + columnCount];
                if(nextPos1 == 0){
                    return true;
                }
                return false;

            }

        }
        else if(this.pieceRotationNumber == 4){

            if(this.pieceNumber == 2){

                this.pieceRotationNumber = 2;
                return this.ValidateRotationMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 2;
                return this.ValidateRotationMovement();

            }

            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 2;
                return this.ValidateRotationMovement();

            }

            else if(this.pieceNumber == 5){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos - columnCount];
                nextPos3 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square2Pos + columnCount + 1];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;

            }

            else if(this.pieceNumber == 6){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[this.square2Pos - columnCount];
                nextPos4 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos3 = this.gameMatrix[this.square2Pos + columnCount - 1];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 7){

                int nextPos1 = this.gameMatrix[this.square2Pos - 1];
                if(nextPos1 == 0){
                    return true;
                }
                return false;

            }

        }
        return false;
    }

    public boolean ValidateLeftMovement() {

        GridLayout grid = findViewById(R.id.gridLayout);
        if(pieceRotationNumber == 1){
            if (this.pieceNumber == 1) {

                int nextPos1, nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];
                if (nextPos1 == 0 && nextPos3 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 3) {

                int nextPos1, nextPos3;
                nextPos3 = this.gameMatrix[this.square3Pos - 1];
                nextPos1 = this.gameMatrix[this.square1Pos - 1];


                if (nextPos3 == 0 && nextPos1 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 4) {

                int nextPos1, nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];

                nextPos3 = this.gameMatrix[this.square3Pos - 1];

                if (nextPos1 == 0 && nextPos3 == 0) {
                    return true;
                }
                return false;


            } else if (this.pieceNumber == 2) {

                int cero = 2;
                int nextPos1, nextPos2, nextPos3, nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos2 = this.gameMatrix[this.square2Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 1];

                if (nextPos1 == 0 && nextPos2 == 0 &&
                        nextPos3 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;

            } else if (this.pieceNumber == 5 || this.pieceNumber == 6) {

                int nextPos1, nextPos2, nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos2 = this.gameMatrix[this.square2Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];

                if (nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0) {
                    return true;
                }
                return false;


            } else if (this.pieceNumber == 7) {

                int nextPos1, nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 1];
                if (nextPos1 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;

            }

        }
        else if(this.pieceRotationNumber == 2){

            if(this.pieceNumber == 1){

                int nextPos1, nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];
                if (nextPos1 == 0 && nextPos3 == 0) {
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 2){

                int nextPos4 = this.gameMatrix[this.square4Pos - 1];
                if(nextPos4 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 3){

                int nextPos2,nextPos3,nextPos4;
                nextPos2 = this.gameMatrix[this.square2Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 1];

                if(nextPos2 == 0 && nextPos3 == 0 && nextPos4 == 0 ){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 4){

                int nextPos1,nextPos3,nextPos4;
                nextPos1 = this.gameMatrix[square1Pos - 1];
                nextPos3 = this.gameMatrix[square3Pos - 1];
                nextPos4 = this.gameMatrix[square4Pos - 1];

                if(nextPos1 == 0 && nextPos3 ==0 && nextPos4 == 0 ){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 5 || this.pieceNumber == 6){

                int nextPos3, nextPos4;
                nextPos3 =  this.gameMatrix[square3Pos - 1];
                nextPos4 = this.gameMatrix[square4Pos - 1];
                if(nextPos3 == 0  && nextPos4 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 7){

                int nextPos1,nextPos3,nextPos4;

                nextPos1 = this.gameMatrix[square1Pos - 1];
                nextPos3 = this.gameMatrix[square3Pos - 1];
                nextPos4 = this.gameMatrix[square4Pos - 1];
                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0 ){
                    return true;
                }
                return false;
            }
        }
        else if(this.pieceRotationNumber == 3){

            if(this.pieceNumber == 1){

                this.pieceRotationNumber = 1;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 2){

                this.pieceRotationNumber = 1;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 1;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 1;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 5 || this.pieceNumber == 6){

                int nextPos4,nextPos2,nextPos1;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos2 = this.gameMatrix[this.square2Pos - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 1];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;

            }

            else if(this.pieceNumber == 7){

                int nextPos3,nextPos4;
                nextPos3 = this.gameMatrix[this.square3Pos - 1 ];
                nextPos4 = this.gameMatrix[this.square4Pos - 1 ];
                if(nextPos3 == 0  && nextPos4 == 0){
                    return true;
                }
                return false;
            }

        }
        else if(this.pieceRotationNumber == 4){

            if(this.pieceNumber == 1){

                this.pieceRotationNumber = 2;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 2){

                this.pieceRotationNumber = 2;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 2;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 2;
                return this.ValidateLeftMovement();

            }
            else if(this.pieceNumber == 5){

                int nextPos4,nextPos1;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos4 = this.gameMatrix[this.square4Pos - 1];

                if(nextPos1 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 6){

                int nextPos1,nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];
                if(nextPos1 == 0 && nextPos3 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 7){


                int nextPos1,nextPos2,nextPos3;

                nextPos1 = this.gameMatrix[this.square1Pos - 1];
                nextPos2 = this.gameMatrix[this.square2Pos - 1];
                nextPos3 = this.gameMatrix[this.square3Pos - 1];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0){
                    return true;
                }
                return false;

            }

        }

        return false;
    }

    public boolean ValidateRightMovement() {



        if(this.pieceRotationNumber == 1){


            if (this.pieceNumber == 1) {

                int nextPos2, nextPos4;
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];

                if (nextPos2 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 2) {

                int nextPos1, nextPos2, nextPos3, nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + 1];
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos3 = this.gameMatrix[this.square3Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];
                if (nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 3 || this.pieceNumber == 4) {

                int nextPos2, nextPos4;
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];
                if (nextPos2 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 5 || this.pieceNumber == 6) {

                int nextPos1, nextPos2, nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + 1];
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];
                if (nextPos1 == 0 && nextPos2 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 7) {

                int nextPos3, nextPos4;
                nextPos3 = this.gameMatrix[this.square3Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];
                if (nextPos3 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            }


        }
        else if(this.pieceRotationNumber == 2){

            if(this.pieceNumber == 1){

                int nextPos2, nextPos4;
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];

                if (nextPos2 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 2){

                int nextPos1 = this.gameMatrix[this.square1Pos + 1];
                if(nextPos1 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 3){

                int nextPos1,nextPos2,nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos +1];
                nextPos2 = this.gameMatrix[this.square2Pos +1];
                nextPos3 = this.gameMatrix[this.square3Pos +1];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0){
                    return true;
                }
                return false;


            }
            else if(this.pieceNumber == 4){

                int nextPos1,nextPos2,nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + 1];
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos4 = this.gameMatrix[this.square4Pos + 1];

                if(nextPos1 ==0 && nextPos2 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }

            else if(this.pieceNumber == 5){

                int nextPos1,nextPos4;
                nextPos4 = this.gameMatrix[this.square4Pos +1];
                nextPos1 = this.gameMatrix[this.square1Pos + 1];

                if(nextPos1 ==0 && nextPos4 ==0){
                    return true;
                }
                return false;


            }
            else if(this.pieceNumber == 6){

                int nextPos1, nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos + 1];
                nextPos3 = this.gameMatrix[this.square3Pos + 1];
                if(nextPos1 == 0 && nextPos3 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 7){

                int nextPos1, nextPos2,nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos + 1];
                nextPos2 = this.gameMatrix[this.square2Pos + 1];
                nextPos3 = this.gameMatrix[this.square3Pos + 1];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 ==0){
                    return true;
                }
                return false;



            }

        }
        else if(this.pieceRotationNumber == 3){

            if(this.pieceNumber == 1){
                this.pieceRotationNumber = 1;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 2){

                this.pieceRotationNumber = 1;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 1;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 1;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 5 || this.pieceNumber == 6){

                int nextPos3,nextPos2,nextPos1;
                nextPos3 = this.gameMatrix[square3Pos + 1];
                nextPos2 = this.gameMatrix[square2Pos + 1];
                nextPos1 = this.gameMatrix[square1Pos + 1];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0 ){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 7){

                int nextPos4,nextPos1;
                nextPos4 = this.gameMatrix[this.square4Pos + 1];
                nextPos1 = this.gameMatrix[this.square1Pos + 1];
                if(nextPos1 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }

        }
        else if(this.pieceRotationNumber == 4){

            if(this.pieceNumber == 1){
                this.pieceRotationNumber = 2;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 2){

                this.pieceRotationNumber = 2;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 2;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 2;
                return this.ValidateRightMovement();

            }
            else if(this.pieceNumber == 5 || this.pieceNumber == 6){

                int nextPos4,nextPos3;
                nextPos4 = this.gameMatrix[this.square4Pos + 1];
                nextPos3 = this.gameMatrix[this.square3Pos + 1];
                if(nextPos3 ==0 && nextPos4 ==0){
                    return true;
                }
                return false;

            }

            else if(this.pieceNumber == 7){

                int nextPos3,nextPos4,nextPos1;
                nextPos3 = this.gameMatrix[square3Pos + 1];
                nextPos4 = this.gameMatrix[square4Pos + 1];
                nextPos1 = this.gameMatrix[square1Pos + 1];

                if(nextPos1 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }

        }


        return false;
    }

    // Checks if the actual piece can move down
    public boolean ValidateDownMovement() {


        GridLayout grid = findViewById(R.id.gridLayout);
        int columnCount = grid.getColumnCount();

        Log.i("ches", String.valueOf(this.pieceRotationNumber) + "  rotation");
        if(this.pieceRotationNumber == 1){

            if (this.pieceNumber == 1){

                int nextPos3, nextPos4;
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

                if (nextPos3 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 2) {

                int nextPos4;
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                if (nextPos4 == 0) {
                    return true;
                }
                return false;

            } else if (this.pieceNumber == 3) {

                int nextPos3, nextPos4, nextPos2;
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                nextPos2 = this.gameMatrix[this.square2Pos + columnCount];

                if (nextPos3 == 0 && nextPos4 == 0 && nextPos2 == 0) {
                    return true;
                }
                return false;

            } else if (this.pieceNumber == 4) {

                int nextPos3, nextPos4, nextPos1;
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                if (nextPos3 == 0 && nextPos4 == 0 && nextPos1 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 5 || this.pieceNumber == 6) {

                int nextPos3, nextPos4;
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                if (nextPos3 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;
            } else if (this.pieceNumber == 7) { // for now , t like piece
                int nextPos1, nextPos3, nextPos4;

                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                if (nextPos4 == 0 && nextPos1 == 0 && nextPos3 == 0) {
                    return true;
                }
                return false;


            }
            return false;

        }

        else if(this.pieceRotationNumber == 2){

            if(this.pieceNumber == 1 || this.pieceNumber == 7){

                int nextPos3, nextPos4;
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

                if (nextPos3 == 0 && nextPos4 == 0) {
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 2){

                int nextPos1,nextPos2,nextPos3,nextPos4;

                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos2 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 3){

                int nextPos2 = this.gameMatrix[this.square2Pos + columnCount];
                int nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

                if(nextPos2 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 4){


                int nextPos2 = this.gameMatrix[this.square2Pos + columnCount];
                int nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                if(nextPos2 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;


            }
            else if(this.pieceNumber == 5 || this.pieceNumber == 6){

                int nextPos1, nextPos2,nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos2 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }

        }
        else if(this.pieceRotationNumber == 3){

            if(this.pieceNumber == 1){
                this.pieceRotationNumber = 1;
                return this.ValidateDownMovement();

            }
            else if(this.pieceNumber == 2){

                this.pieceRotationNumber = 1;
                return this.ValidateDownMovement();

            }
            else if(this.pieceNumber == 3){
                this.pieceRotationNumber = 1;
                return this.ValidateDownMovement();
            }

            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 1;
                return this.ValidateDownMovement();
            }
            else if(this.pieceNumber == 5){

                int nextPos1,nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
                if(nextPos1 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }

            else if(this.pieceNumber == 6){

                int nextPos1,nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                if(nextPos1 == 0 && nextPos3 ==0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 7){

                int nextPos3,nextPos2,nextPos1;
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
                nextPos2 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];

                if (nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0) {
                    return true;
                }
                return false;
            }

        }
        else if(this.pieceRotationNumber == 4){

            if(this.pieceNumber == 1){
                this.pieceRotationNumber = 2;
                this.ValidateDownMovement();

            }
            else if(this.pieceNumber == 2){

                this.pieceRotationNumber = 2;
                this.ValidateDownMovement();

            }
            else if(this.pieceNumber == 3){

                this.pieceRotationNumber = 2;
                this.ValidateDownMovement();

            }
            else if(this.pieceNumber == 4){

                this.pieceRotationNumber = 2;
                this.ValidateDownMovement();
            }
            else if(this.pieceNumber == 5){
                int nextPos1,nextPos2,nextPos3;
                nextPos1 = this.gameMatrix[square1Pos + columnCount];
                nextPos2 = this.gameMatrix[square2Pos + columnCount];
                nextPos3 = this.gameMatrix[square3Pos + columnCount];
                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0){
                    return true;
                }
                return false;

            }
            else if(this.pieceNumber == 6){

                int nextPos1,nextPos2,nextPos3;
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos2 = this.gameMatrix[this.square2Pos + columnCount];
                nextPos3 = this.gameMatrix[this.square3Pos + columnCount];

                if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0 ){
                    return true;
                }
                return false;
            }
            else if(this.pieceNumber == 7){

                int nextPos1,nextPos4;
                nextPos1 = this.gameMatrix[this.square1Pos + columnCount];
                nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

                if(nextPos1 == 0 && nextPos4 == 0){
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void UpdateUi() {


        GridLayout grid = findViewById(R.id.gridLayout);
        ImageView imageView1, imageView2, imageView3, imageView4;
        imageView1 = (ImageView) grid.getChildAt(this.square1Pos);
        imageView2 = (ImageView) grid.getChildAt(this.square2Pos);
        imageView3 = (ImageView) grid.getChildAt(this.square3Pos);
        imageView4 = (ImageView) grid.getChildAt(this.square4Pos);


        imageView1.setImageResource(this.pieceDrawableId);
        imageView2.setImageResource(this.pieceDrawableId);
        imageView3.setImageResource(this.pieceDrawableId);
        imageView4.setImageResource(this.pieceDrawableId);

    }

    public void LowerPiece() {


        this.gameMatrix[square1Pos] = 0;
        this.gameMatrix[square2Pos] = 0;
        this.gameMatrix[square3Pos] = 0;
        this.gameMatrix[square4Pos] = 0;


        this.square1Pos += this.rowCount;
        this.square2Pos += this.rowCount;
        this.square3Pos += this.rowCount;
        this.square4Pos += this.rowCount;

        this.gameMatrix[square1Pos] = 1;
        this.gameMatrix[square2Pos] = 1;
        this.gameMatrix[square3Pos] = 1;
        this.gameMatrix[square4Pos] = 1;
    }

    public void LowerPieceUI() {

        GridLayout grid = findViewById(R.id.gridLayout);
        ImageView imageView1, imageView2, imageView3, imageView4;
        // Clear actual pieces
        this.ClearActualPieces();
        this.LowerPiece();

        //Update UI
        this.UpdateUi();


    }


    public boolean PlacePiece(int square1Pos, int square2Pos, int square3Pos, int square4Pos, int pieceNumber) {

        if (this.gameMatrix[square1Pos] == 0 &&
                this.gameMatrix[square2Pos] == 0 && this.gameMatrix[square3Pos] == 0 &&
                this.gameMatrix[square3Pos] == 0 && this.gameMatrix[square4Pos] == 0){


            this.square1Pos = square1Pos;
            this.square2Pos = square2Pos;
            this.square3Pos = square3Pos;
            this.square4Pos = square4Pos;

            this.gameMatrix[this.square1Pos] = 1;
            this.gameMatrix[this.square2Pos] = 1;
            this.gameMatrix[this.square3Pos] = 1;
            this.gameMatrix[this.square4Pos] = 1;

            this.pieceNumber = pieceNumber;
            this.pieceOnGame = true;


            return true;


        }
        return false;




    }

    public void PlacePieceUi(int square1Pos, int square2Pos, int square3Pos, int square4Pos, int drawable) {

        GridLayout grid = findViewById(R.id.gridLayout);

        ImageView imageView1, imageView2, imageView3, imageView4;

        imageView1 = (ImageView) grid.getChildAt(square1Pos);
        imageView2 = (ImageView) grid.getChildAt(square2Pos);
        imageView3 = (ImageView) grid.getChildAt(square3Pos);
        imageView4 = (ImageView) grid.getChildAt(square4Pos);

        imageView1.setImageResource(drawable);
        imageView2.setImageResource(drawable);
        imageView3.setImageResource(drawable);
        imageView4.setImageResource(drawable);

        this.pieceDrawableId = drawable;

    }

    public void initGridLayout() {

        GridLayout grid = findViewById(R.id.gridLayout);
        int count = grid.getChildCount();
        ImageView imageView;

        Log.i("COUNT", "Hijos: " + count);

        for (int i = 0; i < count; i++) {

            imageView = (ImageView) grid.getChildAt(i);
            Log.i("IV", i + imageView.toString());

            imageView.setImageDrawable(null);
            imageView.setBackground(null);
            imageView.setImageResource(android.R.color.transparent);

        }
    }


    public void initGridMargin() {

        GridLayout grid = findViewById(R.id.gridLayout);

        ImageView imageView;
        int rowCount = grid.getRowCount();
        int columnCount = grid.getColumnCount();
        int lastRowIndex = (rowCount * columnCount) - columnCount;

        // margens de arriba y abajo
        for (int i = 0; i < columnCount; i++) {

            this.gameMatrix[i] = 1;

            this.gameMatrix[i] = i + lastRowIndex;

        }

        //Margenes de los lados izq y der
        int count = (rowCount * columnCount) - columnCount;
        for (int i = 0; i < rowCount * columnCount; i = i + columnCount) {

            this.gameMatrix[i] = 1;
            this.gameMatrix[i + columnCount - 1] = 1;

        }

    }

    public void initGridMarginUI() {

        GridLayout grid = findViewById(R.id.gridLayout);

        ImageView imageView;
        int rowCount = grid.getRowCount();
        int columnCount = grid.getColumnCount();
        int lastRowIndex = (rowCount * columnCount) - columnCount;

        // margens de arriba y abajo
        for (int i = 0; i < columnCount; i++) {

            imageView = (ImageView) grid.getChildAt(i);
            imageView.setImageResource(R.drawable.margintest);

            this.gameMatrix[i] = 1;

            imageView = (ImageView) grid.getChildAt(i + lastRowIndex);
            imageView.setImageResource(R.drawable.margintest);

            this.gameMatrix[i + lastRowIndex] = 1;


        }

        //Margenes de los lados izq y der
        int count = (rowCount * columnCount) - columnCount;
        for (int i = 0; i < rowCount * columnCount; i = i + columnCount) {

            imageView = (ImageView) grid.getChildAt(i);
            imageView.setImageResource(R.drawable.margintest);
            this.gameMatrix[i] = 1;

            imageView = (ImageView) grid.getChildAt(i + columnCount - 1);
            imageView.setImageResource(R.drawable.margintest);
            this.gameMatrix[i + columnCount - 1] = 1;

        }

    }


    public void ClearRow(int firstIndex,int lastIndex) throws InterruptedException {

        GridLayout grid = findViewById(R.id.gridLayout);
        ImageView imageView;

        for(int i = firstIndex; i <= lastIndex; i++){

            imageView = (ImageView) grid.getChildAt(i);
            imageView.setImageResource(android.R.color.transparent);
            imageView.setImageDrawable(null);
            this.gameMatrix[i] = 0;
        }


    }

    public void CheckCompleteRow() throws InterruptedException {

        int index = 0;
        boolean completeRow = true;
        int value = 0 ;
        int lastIndex = 0;
        int firstIndex = 0;

        for(int i = rowCount - 2; i >= 1; i--){
            //Log.i("letra" , " Fila : " + Integer.toString(i));
            for(int j = columnCount - 2 ;j >= 1; j--){

                index = (rowCount * i) + j;
                value = this.gameMatrix[index];

                lastIndex = index;

                if(j == rowCount - 2){
                    firstIndex = index;
                }
                //Log.i("letra" , "Columna: " + Integer.toString(j));
                //Log.i("letra" , "Valor en matriz: " + Integer.toString(value));
                //Log.i("index " , Integer.toString(index));

                if(value == 0){
                    completeRow = false;
                    break;
                }
            }
            if(completeRow){


                this.ClearRow(lastIndex,firstIndex);
                //Move down all pieces
                this.MoveDownAllPieces();
                this.CheckCompleteRow();
            }
            completeRow = true;

        }

    }

    public void MoveDownAllPieces() {


        int index = 0;
        int value = 0;

        for (int i = rowCount -2 ; i >= 1; i--) {

            for (int j = this.columnCount - 2; j >= 1; j--) {

                index = (rowCount * i) + j;
                value = this.gameMatrix[index];

                if(value == 1 && this.gameMatrix[index + columnCount] == 0){

                    int temporalIndex = index;
                    do {
                        GridLayout gridLayout = findViewById(R.id.gridLayout);
                        ImageView imageViewOriginal = (ImageView)gridLayout.getChildAt(temporalIndex);
                        ImageView imageViewNueva = (ImageView)gridLayout.getChildAt(temporalIndex + columnCount);
                        imageViewNueva.setImageDrawable(imageViewOriginal.getDrawable());

                        imageViewOriginal.setImageResource(android.R.color.transparent);
                        imageViewOriginal.setImageDrawable(null);
                        imageViewOriginal.setBackground(null);

                        this.gameMatrix[temporalIndex] = 0 ;
                        this.gameMatrix[temporalIndex + columnCount] = 1;

                        temporalIndex += columnCount;

                    }while(this.gameMatrix[temporalIndex + columnCount] == 0);

                }
            }
        }
    }

    // Buttons

    public void OnClickButtonDown(View view) throws InterruptedException {


        if (this.pieceOnGame &&  this.ValidateDownMovement() ) {

            this.semaphore.acquire();
            this.LowerPieceUI();
            this.semaphore.release();

        }

    }

    public void OnClickButtonLeft(View view) throws InterruptedException {


        if ( this.pieceOnGame && this.ValidateLeftMovement() ) {

            this.semaphore.acquire();
            this.MoveLeftUI();
            this.semaphore.release();
        }

    }

    public void OnClickButtonRight(View view) throws InterruptedException {

        if (this.pieceOnGame &&  this.ValidateRightMovement()) {

            this.semaphore.acquire();
            this.MoveRightUI();
            this.semaphore.release();
        }
    }

    public void OnClickButtonRotate(View view) throws InterruptedException {

        if(this.pieceOnGame &&  this.ValidateRotationMovement()){

            this.semaphore.acquire();
            this.RotateUI();
            this.semaphore.release();
        }
    }
}

