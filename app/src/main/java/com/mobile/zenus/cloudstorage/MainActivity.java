package com.mobile.zenus.cloudstorage;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private StorageReference mStorageRef;

    private LinearLayout mImagens;
    private ProgressBar mProgressBar;
    private TextView mResultado;

    private Button mBtnDownload;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            atualizarUI( msg.getData().getString("mensagem"));

            }

        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mImgView = (ImageView) findViewById(R.id.imgDownload);
//
//        mImgView.setDrawingCacheEnabled(true);
//        mImgView.buildDrawingCache();

        mImagens = (LinearLayout) findViewById(R.id.imagens);

        mProgressBar = (ProgressBar) findViewById(R.id.firstBar);

        mResultado = (TextView) findViewById(R.id.txtResultado);

        mBtnDownload = (Button) findViewById(R.id.btnDownload);
    }

        private void atualizarUI(String mensagem){

            mResultado.setText(mensagem);
        }



    public void download(View views) throws IOException {

        mProgressBar.setSoundEffectsEnabled(true);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setMax(20);

        final StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cloudstorage-77f43.appspot.com/aranha.jpg");

        final File localFile = File.createTempFile("aranha", "jpg");

        gsReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


//                        mImgView.setVisibility(View.VISIBLE);
//                        Picasso.with(getBaseContext()).load(localFile).into(mImgView);

                        for (int i = 0; i < 20; i++) {
                            mProgressBar.setProgress(i + 1);

                            Message mensagem = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("mensagem", "" + (i+1)+" imagens");
                            mensagem.setData(bundle);

                            mHandler.sendMessage(mensagem);

                            TableRow itemRow = new TableRow(getBaseContext());

                            TextView textView = new TextView(getBaseContext());

                            ImageView imagem = new ImageView(getBaseContext());
                            imagem.setDrawingCacheEnabled(true);
                            imagem.buildDrawingCache();

                            Picasso.with(getBaseContext()).load(localFile).into(imagem);

                            itemRow.addView(imagem);

                            mImagens.addView(itemRow);


                        }

                        mProgressBar.setVisibility(View.GONE);
                        mBtnDownload.setEnabled(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {


                Toast.makeText(getBaseContext(), "DOWLOAD ERROR", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
