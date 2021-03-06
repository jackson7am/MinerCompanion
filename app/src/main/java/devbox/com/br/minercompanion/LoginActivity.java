/**
 *MinerCompanion - Sistema de Alerta para Mineradoras
 *Copyright (C) <2015>  <Jackson Lima, Jean Figueiredo, Victor Valente>
 *
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package devbox.com.br.minercompanion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity {

<<<<<<< HEAD
    //String url = "http://192.168.49.254/miner_companion/admin_server/requests.php";
    String url = "http://32f35102.ngrok.com/miner_companion/admin_server/requests.php";
=======
    String url = "http://32f35102.ngrok.com/miner_companion/admin_server/requests.php";

>>>>>>> origin/victor
    EditText editText;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createProgressDialog();

        editText = (EditText) findViewById(R.id.editText);

        Button login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);

                //if(editText.getText().length() > 0 && isConnected()) {
                if(editText.getText().length() > 0) {
                    progressDialog.show();
                    new HttpAsyncTask().execute(url, editText.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Matrícula não preenchida!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Conectando-se ao servidor!");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... data) {

            return POST(data[0], data[1]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("HTTP REQUEST RESULTADO", result);

            progressDialog.dismiss();

            if(result.contains("OK")) {
                //Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                intent.putExtra("MATRICULA", editText.getText().toString());
                startActivity(intent);
                finish();


            } else {
                Toast.makeText(getBaseContext(), "Erro ao tentar conectar-se com o servidor!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        Log.d("HTTP REQUEST RESULTADO", "");
        while((line = bufferedReader.readLine()) != null) {
            result = line;
            Log.d("", result);
        }
        Log.d("HTTP REQUEST RESULT FIM", "");

        inputStream.close();
        return result;
    }

    public static String POST(String url, String data){
        InputStream inputStream = null;
        String result = "";

        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //String usuarios = jsonParser.getAllUsuariosAsJson().toString();

                //Log.i("USUARIOS", usuarios);
                Log.d("Dados sendo enviado: ", data);



                //params.add(new BasicNameValuePair("usuarios", usuarios));
                params.add(new BasicNameValuePair("login", data));

                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse response = httpclient.execute(httpPost);

                inputStream = response.getEntity().getContent();

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Resultado Nulo";

        } catch (Exception e) {
            Log.i("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    // Just for test connectivity
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(this, "Sem acesso a internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
