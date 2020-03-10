package com.famtechnology.funticket.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.fesoft.tapcolors.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ailton on 11/04/2017.
 */

public class Util {

    public static String getString(Context context, String refString) {

        String txt;

        try {
            String packageName = context.getPackageName();
            int resId = context.getResources().getIdentifier(refString, "string", packageName);
            txt = context.getString(resId);
        } catch (Exception err) {
            txt = "";
        }

        return txt;
    }

    /**
     * Abre o perfil do Linkedin
     * @throws Exception
     */
    public static void openLinkedinProfile(Context context, String perfilId) throws Exception {
        //Cria uma intent informando que deseja abrir o Linkedin com o perfil desejado
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + perfilId));

        //Verifica se o Linkedin está instalado
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        //Se não está, usa o browser como acesso
        if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + perfilId));
        }

        //Inicia o componente
        context.startActivity(intent);
    }

    public static void openKeyboard(final Context context, final View view, final long after) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager methodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                } catch (NullPointerException error) {
                    Crashlytics.logException(error);
                    error.printStackTrace();
                }
            }
        }, after);
    }

    /**
     * Change de app language
     * @param context App context
     * @param lang Language to change. Ex. en, es, pt
     */
    public static void changeLang(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = context.getResources().getConfiguration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Formata um valor para a moeda local. Ex. 3,65 = R$ 3,65
     * @param valor O valor a ser convertido
     * @return Uma String com o valor total
     * @throws Exception
     */
    public static String formatarParaMoedaLocal(double valor) throws Exception {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return numberFormat.format(valor);
    }

    /**
     * Check if the device is connected with the internet
     * @param context App context
     * @return True if its connected, false otherwise.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null) && activeNetwork.isConnected();
    }

    /**
     * Retorna o JSON do arquivo lido do diretorio raw
     * @param rawId O id do arquivo que esta no diretorio raw. Ex. R.raw.produtos
     * @return O json em formato de string
     * @throws Exception
     */
    public static String retornarJsonRaw(Context context, int rawId) throws Exception {
        //Abre o arquivo JSON de carga inicial para inserir no banco
        InputStream inputStream = context.getResources().openRawResource(rawId); //abre o arquivo
        int tamanho = inputStream.available(); //Identifica a quantidade de bytes
        byte[] buffer = new byte[tamanho]; //Cria o array de bytes
        inputStream.read(buffer); //Lê os bytes
        inputStream.close(); //Fecha a leitura

        //Obtem o JSON de produtos
        return new String(buffer, "Windows-1252");
    }

    /**
     * Retorna uma substring baseado em um caracter de inicio e um de fim.<bR>
     *
     * Chamando por limparString(string, '=', '!');<br>
     * Exemplo: String    blabla=sohmeretornessaparte! blablabla<br>
     * Retorna: sohmeretornessaparte <br><br>
     *
     * @param string A string a ser limpada
     * @param doChar O caracter de inicio
     * @param ateChar O caracter de fim
     * @return A string limpa
     * @throws Exception
     */
    public static String limparString(String string, char doChar, char ateChar) throws Exception {
        String retorno = "";
        boolean adicionar = false;

        //Percorre toda a string
        for (int i = 0; i < string.length(); i++) {

            //Se chegou onde devemos parar de insert
            if (adicionar && string.charAt(i) == ateChar) {
                break;
            }

            if (adicionar)
                retorno += string.charAt(i);

            //Se chegou onde devemos começar a insert
            if (string.charAt(i) == doChar)
                adicionar = true;
        }

        return retorno;
    }

    /**
     * Fecha o teclado
     */
    public static void closeKeyboard(Activity activity)
    {
        //Pega o input method do sistema
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus(); //Recupera a view que esta com o foco atual

        //Se for nulo o foco
        if (view == null) {
            view = new View(activity); //Instancia nova view
        }

        //Esconde o teclado
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Copia o banco de dados para a pasta raiz do sistema
     * @param context O contexto da senha
     */
    public static void copyDbToExternal(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + context.getApplicationContext().getPackageName() + "/databases/"
                        + Param.DATABASE_NAME;
                String backupDBPath = Param.DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    /**
     * Verifica se um email é valido
     * @param email O email a ser validado
     * @return True se for valido, false caso não seja
     */
    public static boolean isEmailValid(String email) {
        if ((email == null) || (email.trim().length() == 0))
            return false;

        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Open playstore to rate
     */
    public static void openPlaystorePage(Context context, String appPackage) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.playstore_market_intent) + appPackage)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.playstore_url) + appPackage)));
        }
    }
}
