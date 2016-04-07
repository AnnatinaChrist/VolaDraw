package com.gnuproducts.voladraw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.lobsterpicker.LobsterPicker;

import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {

    private DrawingView drawingView;

    private ImageButton drawBtn, eraseBtn, newBtn, saveBtn, opacityBtn, pencilBtn;
    private ImageButton pickerBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = (DrawingView) findViewById(R.id.drawing);

        drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        pickerBtn = (ImageButton) findViewById(R.id.picker_btn);
        pickerBtn.setOnClickListener(this);

        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        drawingView.setDrawingCacheEnabled(true);

        opacityBtn = (ImageButton) findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        pencilBtn = (ImageButton) findViewById(R.id.pencil_btn);
        pencilBtn.setOnClickListener(this);








    }

    @Override
    public void onClick(View v) {
        //draw button clicked
        if (v.getId() == R.id.draw_btn)

        {
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Brush Size:");
            seekDialog.setContentView(R.layout.brush_size_chooser);
            final TextView seekTxt = (TextView) seekDialog.findViewById(R.id.brush_txt);
            final SeekBar seekBrush = (SeekBar) seekDialog.findViewById(R.id.brush_seek);
            seekBrush.setMax(75);
            float currLevel = 5;
            seekTxt.setText(currLevel + "%");
            seekBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            Button sizeBtn = (Button) seekDialog.findViewById(R.id.brush_ok);
            sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setBrushSize(seekBrush.getProgress());
                    drawingView.setErase(false);
                    seekDialog.dismiss();

                }
            });
            seekDialog.show();

            //button for a new drawing is clicked
        } else if (v.getId() == R.id.new_btn) {

            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Start New Drawing");
            newDialog.setMessage("Start a new drawing (the current drawing will be lost)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawingView.startNew();
                    dialog.dismiss();
                }


            });
            newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            newDialog.show();

            //save button clicked
        } else if (v.getId() == R.id.save_btn) {
            drawingView.setDrawingCacheEnabled(true);
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawingView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawingView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            saveDialog.show();



        }


        //When eraser button is clicked
        else if (v.getId() == R.id.erase_btn) {
            final Dialog eraserDialog = new Dialog(this);
            eraserDialog.setTitle("Eraser Size:");
            eraserDialog.setContentView(R.layout.brush_size_chooser);
            final TextView seekTxt = (TextView) eraserDialog.findViewById(R.id.brush_txt);
            final SeekBar seekBrush = (SeekBar) eraserDialog.findViewById(R.id.brush_seek);
            seekBrush.setMax(75);
            float currLevel = 5;
            seekTxt.setText(currLevel + "%");
            seekBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            Button sizeBtn = (Button) eraserDialog.findViewById(R.id.brush_ok);
            sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setBrushSize(seekBrush.getProgress());
                    drawingView.setErase(true);
                    eraserDialog.dismiss();


                }

            });

            eraserDialog.show();

        }

        //Button for Picker is clicked
        else if (v.getId() == R.id.picker_btn) {

            final Dialog pickerDialog = new Dialog(this);
            pickerDialog.setTitle("Color Picker");
            pickerDialog.setContentView(R.layout.picker_layout);



            /* When the Accept button is clicked, the chosen colour is set as the colour used for
             * drawing on the canvas */
            Button colorBtn = (Button) pickerDialog.findViewById(R.id.color_ok);
            colorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LobsterPicker lobsterPicker = (LobsterPicker) pickerDialog.findViewById(R.id.lobsterpicker);

                    drawingView.setErase(false);
                    int color = lobsterPicker.getColor();
                    drawingView.setColor(color);
                    pickerDialog.dismiss();

                }


            });
            Button cancelBtn = (Button) pickerDialog.findViewById(R.id.color_cancel);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickerDialog.dismiss();
                }
            });

            pickerDialog.show();


        }
        //When opacity button is clicked
        else if (v.getId() == R.id.opacity_btn) {

            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity Level");
            seekDialog.setContentView(R.layout.opacity_chooser);

            final TextView seekTxt = (TextView) seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar) seekDialog.findViewById(R.id.opacity_seek);
            seekOpq.setMax(100);

            int currLevel = drawingView.getPaintAlpha();


            seekTxt.setText(currLevel + "%");
            seekOpq.setProgress(currLevel);
            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            Button opqBtn = (Button) seekDialog.findViewById(R.id.opq_ok);
            opqBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setPaintAlpha(seekOpq.getProgress());
                    seekDialog.dismiss();
                }
            });

            seekDialog.show();

        }
        if (v.getId()== R.id.pencil_btn){

            drawingView.setPencil();


        }




    }


    //Button for a pattern is clicked
    public void paintClicked(View view){

        final Dialog patternDialog = new Dialog(this);
        patternDialog.setContentView(R.layout.pattern_chooser);

        ImageButton patternOk = (ImageButton) patternDialog.findViewById(R.id.pattern1);
        patternOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = v.getTag().toString();
                drawingView.setPattern(pattern);
                patternDialog.dismiss();

            }
        });
        patternDialog.show();

    }



}
