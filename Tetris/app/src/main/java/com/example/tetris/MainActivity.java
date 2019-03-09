package com.example.tetris;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends AppCompatActivity {

    int square1Pos;
    int square2Pos;
    int square3Pos;
    int square4Pos;

    int rowCount;
    int columnCount;

    int gameMatrix [];
    int pieceNumber;
    int pieceDrawableId;
    boolean pieceOnGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        this.initGridLayout();


        this.initGridMarginUI();
        //this.initGridMargin();
        Log.i("matrix", this.gameMatrixToString());

        // Runnable
        final Handler handler = new Handler();
        Runnable r = new Runnable() {

            public void run() {

                //Log.i("ches", "ches");

                if(pieceOnGame){

                    Log.i("Ches", "Pieza on game");
                    if(ValidateDownMovement()){

                        LowerPieceUI();
                        Log.i("Ches", "Pieza bajada");
                    }
                    else{
                        Log.i("Ches", "No se pudo bajar");
                        pieceOnGame = false;
                    }

                }
                else{

                    // Spawn piece
                    SpawnPiece();
                    pieceOnGame = true;
                }

                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(r,1000);

        // End Runnable

    }



    public void SpawnPiece(){

        // Gen randon number

        Random ran = new Random();
        int pieceToGenerate = ran.nextInt(7) + 1;

        Log.i("random",String.valueOf(pieceToGenerate));

        //This works
        this.pieceNumber = pieceToGenerate;


        if(pieceNumber == 1){

            this.PlacePieceUi(14,15,24,25,R.drawable.yellowcelltest);
            this.PlacePiece(14,15,24,25,pieceToGenerate);

        }
        else if(pieceNumber == 2){

            this.PlacePieceUi(14,24,34,35,R.drawable.bluecelltest);
            this.PlacePiece(14,24,34,35,pieceToGenerate);

        }
        else if(pieceNumber == 3){

            this.PlacePieceUi(14,15,23,24,R.drawable.redcelltest);
            this.PlacePiece(14,15,23,24,pieceToGenerate);

        }
        else if(pieceNumber == 4){

            this.PlacePieceUi(13,14,24,25,R.drawable.greencelltest);
            this.PlacePiece(13,14,24,25,pieceToGenerate);

        }
        else if(pieceNumber == 5){

            this.PlacePieceUi(14,24,34,35,R.drawable.orangecelltest);
            this.PlacePiece(14,24,34,35,pieceToGenerate);

        }
        else if(pieceNumber == 6){

            this.PlacePieceUi(14,24,34,33,R.drawable.pinkcelltest);
            this.PlacePiece(14,24,34,33,pieceToGenerate);

        }
        else if(pieceNumber == 7){

            this.PlacePieceUi(13,14,15,24,R.drawable.purplecelltest);
            this.PlacePiece(13,14,15,24,pieceToGenerate);

        }


    }

    public String gameMatrixToString(){

        GridLayout grid = findViewById(R.id.gridLayout);
        int count = grid.getColumnCount() * grid.getRowCount();
        String result = "";
        int number = 0 ;
        for(int i = 0 ; i < count ; i++){

            number = this.gameMatrix[i];
            result += String.valueOf(number) + " -> ";


        }
        return result;
    }



    public void ClearActualPieces(){

        GridLayout grid  = findViewById(R.id.gridLayout);
        // Clear actual pieces
        ImageView imageView1,imageView2,imageView3,imageView4;
        imageView1 = (ImageView)grid.getChildAt(this.square1Pos);
        imageView2 = (ImageView)grid.getChildAt(this.square2Pos);
        imageView3 = (ImageView)grid.getChildAt(this.square3Pos);
        imageView4 = (ImageView)grid.getChildAt(this.square4Pos);

        imageView1.setImageResource(android.R.color.transparent);
        imageView2.setImageResource(android.R.color.transparent);
        imageView3.setImageResource(android.R.color.transparent);
        imageView4.setImageResource(android.R.color.transparent);
    }


    public void MoveLeft(){

        this.square1Pos -= 1;
        this.square2Pos -= 1;
        this.square4Pos -= 1;
        this.square3Pos -= 1;

    }
    public void MoveLeftUI(){

        GridLayout grid = findViewById(R.id.gridLayout);
        ImageView imageView1, imageView2, imageView3,imageView4;
        this.ClearActualPieces();

        MoveLeft();

        this.UpdateUi();





    }
    public boolean ValidateLeftMovement(){

        GridLayout grid = findViewById(R.id.gridLayout);

        // 1 3 4 are the same for going left
        if(this.pieceNumber == 1 || this.pieceNumber == 3 || this.pieceNumber == 4){

            int nextPos1,nextPos3;
            nextPos1 = this.gameMatrix[this.square1Pos -1];
            nextPos3 = this.gameMatrix[this.square3Pos -1];
            if(nextPos1 == 0 && nextPos3 == 0){
                return true;
            }
            return false;
        }
        else if(this.pieceNumber == 2){

            int nextPos1,nextPos2,nextPos3,nextPos4;
            nextPos1 = this.gameMatrix[this.square1Pos-1];
            nextPos2 = this.gameMatrix[this.square2Pos-1];
            nextPos3 = this.gameMatrix[this.square3Pos-1];
            nextPos4 = this.gameMatrix[this.square4Pos-1];

            if(nextPos1 == 0 && nextPos2 == 0 &&
                    nextPos3 == 0 && nextPos4 == 0){
                return true;
            }
            return false;

        }


        else if(this.pieceNumber == 5 || this.pieceNumber == 6){

            int nextPos1,nextPos2,nextPos3;
            nextPos1 = this.gameMatrix[this.square1Pos -1];
            nextPos2 = this.gameMatrix[this.square2Pos -1];
            nextPos3  = this.gameMatrix[this.square3Pos -1];

            if(nextPos1 == 0 && nextPos2 == 0 && nextPos3 == 0){
                return true;
            }
            return false;


        }
        else if(this.pieceNumber == 7){

            int nextPos1,nextPos4;
            nextPos1 = this.gameMatrix[this.square1Pos -1];
            nextPos4 = this.gameMatrix[this.square4Pos -1];
            if(nextPos1 == 0 && nextPos4 == 0){
                return true;
            }
            return false;

        }

        return false;
    }

    public boolean ValidateRightMovement(){

        return false;
    }
    // Checks if the actual piece can move down
    public boolean ValidateDownMovement(){


        GridLayout grid = findViewById(R.id.gridLayout);

        if(this.pieceNumber == 1){

            int nextPos3, nextPos4;
            nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

            if(nextPos3 == 0 && nextPos4 == 0){

                return true;
            }
            return false;
        }
        else if(this.pieceNumber == 2){

            int nextPos4;
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
            if(nextPos4 == 0){
                return true;
            }
            return false;

        }
        else if(this.pieceNumber == 3){

            int nextPos3, nextPos4;
            nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

            if(nextPos3 == 0 && nextPos4 == 0){
                return true;
            }
            return false;

        }
        else if(this.pieceNumber == 4){

            int nextPos3, nextPos4;
            nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
            if(nextPos3 == 0 && nextPos4 == 0){
                return true;
            }
            return false;
        }

        else if(this.pieceNumber == 5){

            int nextPos3,nextPos4;
            nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
            if(nextPos3 == 0 && nextPos4 == 0){
                return true;
            }
            return false;
        }
        else if(this.pieceNumber == 6){

            int nextPos3,nextPos4;
            nextPos3 = this.gameMatrix[this.square3Pos + columnCount];
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];
            if(nextPos3 == 0 && nextPos4 == 0){
                return true;
            }
            return false;

        }
        else if(this.pieceNumber == 7){ // for now , t like piece


            int nextPos4;
            columnCount = grid.getColumnCount();
            nextPos4 = this.gameMatrix[this.square4Pos + columnCount];

            if (nextPos4 == 0){
                return true;
            }
            return false;

        }
        return false;

    }

    public void UpdateUi(){

        GridLayout grid = findViewById(R.id.gridLayout);
        ImageView imageView1,imageView2,imageView3,imageView4;
        imageView1 = (ImageView) grid.getChildAt(this.square1Pos);
        imageView2 = (ImageView) grid.getChildAt(this.square2Pos);
        imageView3 = (ImageView) grid.getChildAt(this.square3Pos);
        imageView4 = (ImageView) grid.getChildAt(this.square4Pos);


        imageView1.setImageResource(this.pieceDrawableId);
        imageView2.setImageResource(this.pieceDrawableId);
        imageView3.setImageResource(this.pieceDrawableId);
        imageView4.setImageResource(this.pieceDrawableId);
    }
    public void LowerPiece(){


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

    public void LowerPieceUI(){

        GridLayout grid = findViewById(R.id.gridLayout);
        ImageView imageView1,imageView2,imageView3,imageView4;
        // Clear actual pieces
        this.ClearActualPieces();
        this.LowerPiece();

        //Update UI
        this.UpdateUi();



    }


    public void PlacePiece(int square1Pos, int square2Pos, int square3Pos, int square4Pos,int pieceNumber){

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

    }
    public void PlacePieceUi(int square1Pos, int square2Pos, int square3Pos, int square4Pos, int drawable){

        GridLayout grid = findViewById(R.id.gridLayout);

        ImageView imageView1, imageView2, imageView3,imageView4;

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

        for(int i = 0 ; i < count; i++){

            imageView = (ImageView) grid.getChildAt(i);
            Log.i("IV", i + imageView.toString() );

            //imageView.setImageDrawable(null);
            imageView.setBackground(null);
            //imageView.setImageResource(android.R.color.transparent);

        }
    }


    public void initGridMargin(){

        GridLayout grid = findViewById(R.id.gridLayout);

        ImageView imageView;
        int rowCount = grid.getRowCount();
        int columnCount = grid.getColumnCount();
        int lastRowIndex = (rowCount*columnCount) - columnCount;

        // margens de arriba y abajo
        for(int i = 0; i < columnCount;i++){

            this.gameMatrix[i] = 1;

            this.gameMatrix[i] = i + lastRowIndex;

        }

        //Margenes de los lados izq y der
        int count = (rowCount * columnCount) - columnCount;
        for(int i = 0 ; i < rowCount * columnCount; i = i + columnCount){

            this.gameMatrix[i] = 1;
            this.gameMatrix[i + columnCount -1] = 1;

        }

    }
    public void initGridMarginUI(){

        GridLayout grid = findViewById(R.id.gridLayout);

        ImageView imageView;
        int rowCount = grid.getRowCount();
        int columnCount = grid.getColumnCount();
        int lastRowIndex = (rowCount*columnCount) - columnCount;

        // margens de arriba y abajo
        for(int i = 0; i < columnCount;i++){

            imageView = (ImageView)grid.getChildAt(i);
            imageView.setImageResource(R.drawable.margintest);

            this.gameMatrix[i] = 1;

            imageView =(ImageView) grid.getChildAt(i + lastRowIndex);
            imageView.setImageResource(R.drawable.margintest);

            this.gameMatrix[i + lastRowIndex] = 1;


        }

        //Margenes de los lados izq y der
        int count = (rowCount * columnCount) - columnCount;
        for(int i = 0 ; i < rowCount * columnCount; i = i + columnCount){

            imageView = (ImageView)grid.getChildAt(i);
            imageView.setImageResource(R.drawable.margintest);
            this.gameMatrix[i] = 1;

            imageView = (ImageView)grid.getChildAt( i + columnCount - 1);
            imageView.setImageResource(R.drawable.margintest);
            this.gameMatrix[i + columnCount - 1] = 1;

        }

    }

    // Buttons

    public void OnClickButtonDown(View view){


        if(this.ValidateDownMovement()){
            this.LowerPieceUI();

        }

    }

    public void OnClickButtonLeft(View view){


        if(this.ValidateLeftMovement()){

            this.MoveLeftUI();
        }

    }
    public void OnClickButtonRight(View view){

    }
}
