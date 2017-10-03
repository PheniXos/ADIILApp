package justin_martin.adiilapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements Constant{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnexion();
    }

    public void checkConnexion(){
        if(isOnline()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Connexion");
            alertDialog.setMessage(Constant.NETWORK_ERROR);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Réssayer",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    checkConnexion();
                }
            } );

            alertDialog.show();

        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
