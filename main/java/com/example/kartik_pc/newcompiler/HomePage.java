package com.example.kartik_pc.newcompiler;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;


public class HomePage extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String testcases = "";
    String languages[] = {"    Languages ","    C     ","    C++      ","    JAVA     ","    C#       ","    PHP      ","    RUBY     ","    PYTHON       ","    PERL     ","    HASKEL       ","    CLOJURE      ","    SCALA        ","    BASH     ","    ERLANG       ","    CLISP       ","     LUA     ","     GO        "};
    static int language_codes[]={0,1,2,3,9,7,8,5,6,12,13,15,14,16,17,18,21};
    String extension[] = {"","c","cpp","java","cs","php","ruby","py","pl","hs","clj","scala","sh","hrl","lisp","lua","go"};
    HashMap<String,Integer> hm = new HashMap<String,Integer>();
    String code_templates[] = {"","#include <stdio.h>\n#include <string.h>\n#include <math.h>\n#include <stdlib.h>\n" +
            "#define MOD 1000000007\n#define s(n) scanf(\"%d\",&n)\n#define p(n) printf(\"%d\\n\",n)\n" +
            "#define ss(n) scanf(\"%lld\",&n)\n#define pp(n) printf(\"%lld\\n\",n)\n" +"typedef long long int ll;\n" +
            "int main()\n" +
            "{\n/*Your code goes here*/\n\n\n\nreturn 0;\n}",
            "#include <bits/stdc++.h>\n" +
            "using namespace std;\n" +
            "#define mod 1000000007\n" +
            "#define s(n) scanf(\"%d\",&n)\n" +
            "#define p(n) printf(\"%d\\n\",n)\n" +
            "#define ss(n) scanf(\"%lld\",&n)\n" +
            "#define pp(n) printf(\"%lld\\n\",n)\ntypedef long long int ll;\n" +
            "int main()\n" + "{\n/*Your code goes here*/\n\n\n\nreturn 0;\n}",
            "import java.io.*;\n" +
            "import java.util.Arrays;\n" +
            "import java.util.*;\npublic class Main {\n" +
            "public static void main(String[] args) {\n /*Your code goes here.*/\n\n\n\n}\n}\n",

            "using System;\n" +
            "\n" +
            "public class Test\n" +
            "{\n" +
            "\tpublic static void Main()\n" +
            "\t{\n" +
            "\t\t// your code goes here\n" +
            "\t}\n" +
            "}\n",

            "<?php\n" +
            "\n" +
            "// your code goes here\n",

            "# your code goes here\n",

            "# your code goes here\n",

            "#!/usr/bin/perl\n" +
            "# your code here\n",

            "-- Type annotation (optional)\n" +
            "fib :: Int -> Integer\n" +
            " \n" +
            "-- With self-referencing data\n" +
            "fib n = fibs !! n\n" +
            "        where fibs = 0 : scanl (+) 1 fibs\n" +
            "        -- 0,1,1,2,3,5,...\n" +
            " \n" +
            "-- Same, coded directly\n" +
            "fib n = fibs !! n\n" +
            "        where fibs = 0 : 1 : next fibs\n" +
            "              next (a : t@(b:_)) = (a+b) : next t\n" +
            " \n" +
            "-- Similar idea, using zipWith\n" +
            "fib n = fibs !! n\n" +
            "        where fibs = 0 : 1 : zipWith (+) fibs (tail fibs)\n" +
            " \n" +
            "-- Using a generator function\n" +
            "fib n = fibs (0,1) !! n\n" +
            "        where fibs (a,b) = a : fibs (b,a+b)",

            "; your code goes here\n","object Main extends App {\n" +
            "\t// your code goes here\n" +
            "}\n",

            "/* your code goes here */\n",

            "-module(prog).\n" +
            "-export([main/0]).\n" +
            "\n" +
            "main() ->\n" +
            "\t% your code goes here\n" +
            "\ttrue.\n",

            "",

            "-- your code goes here\n",

            "package main\n" +
            "import \"fmt\"\n" +
            "\n" +
            "func main(){\n" +
            "\t// your code goes here\n" +
            "}\n"};

    Spinner lang;
    TextView prog_name;
    EditText editor;
    EditText open_file1;
    ImageButton run;
    static int lang_selected = 0;
    static int curr_lang = 0;
    static int prev_lang = 0;
    static int FILE_OPEN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        hm.put("c", 1);
        hm.put("cpp",2);
        hm.put("java",3);
        hm.put("cs",4);
        hm.put("php",5);
        hm.put("ruby",6);
        hm.put("py",7);
        hm.put("pl",8);
        hm.put("hs",9);
        hm.put("clj",10);
        hm.put("scala",11);
        hm.put("sh",12);
        hm.put("hrl",13);
        hm.put("lisp",14);
        hm.put("lua",15);
        hm.put("go",16);

        open_file1 = (EditText) findViewById(R.id.open_file_bro);
        prog_name = (TextView) findViewById(R.id.prog_name);
        editor = (EditText) findViewById(R.id.editor);
        run = (ImageButton) findViewById(R.id.title1);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lang = (Spinner)findViewById(R.id.spinner_lang);
        ArrayAdapter<String> adp= new ArrayAdapter<String>(this,R.layout.scrolview, languages);
        lang.setAdapter(adp);

        SharedPreferences SP = getSharedPreferences("data",0);
        editor.setText(SP.getString("edit",""));
        lang.setSelection(SP.getInt("lang", 0));
        prog_name.setText(SP.getString("progname", prog_name.getText().toString()));
        prog_name.setTextColor(Color.BLACK);
        prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
        FILE_OPEN = SP.getInt("cur_open_lang",0);


        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr_lang == 0) {
                    Toast.makeText(HomePage.this, "No language selected\nSelect a language first!", Toast.LENGTH_SHORT).show();
                } else if (editor.getText().toString().equals("")) {
                    Toast.makeText(HomePage.this, "No code inserted\nPlease insert code", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            testcases = "1\n";
                            Intent intent = new Intent(HomePage.this, HackerRankAPIHandler.class);
                            intent.putExtra("test_cases1", "1\n");
                            testcases = "";
                            intent.putExtra("source_code1", editor.getText().toString());
                            intent.putExtra("language_selected1", String.valueOf(language_codes[curr_lang]));
                            startActivity(intent);
                            Toast.makeText(HomePage.this, "Connected", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(HomePage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                curr_lang = position;
                lang.setSelection(curr_lang);
                //prog_name.setTextColor(Color.BLACK);
                //prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                    if(FILE_OPEN == 0) {
                        editor.setText(code_templates[curr_lang]);
                        prog_name.setText("**File Name");
                    }
                if(FILE_OPEN == 1)
                    FILE_OPEN = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); // close setOnItemSelectedListener

    }//close onCreate

    private void addDrawerItems() {
        String[] DrawerArray = { "Run Your Code","Testcases", "New File", "Open File", "Save File","Save As", "About Us" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DrawerArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {//New File
                    //LayoutInflater l1 = LayoutInflater.from(HomePage.this);
                    //View dialog_view1 = l1.inflate(R.layout.new_file, null);
                    //AlertDialog.Builder new_file = new AlertDialog.Builder(HomePage.this);
                    //new_file.setView(dialog_view1);
                    AlertDialog.Builder new_file = new AlertDialog.Builder(HomePage.this);
                    new_file.setTitle("File Changed!\nWanna save it?");
                    //new_file.setMessage("Welcome");

                    new_file.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /*int f=0;
                            if (curr_lang == 0) {
                                Toast.makeText(HomePage.this, "Select Language First", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                f=1;
                            }*/

                            LayoutInflater li = LayoutInflater.from(HomePage.this);
                            View dialog_view = li.inflate(R.layout.save_file, null);
                            AlertDialog.Builder save_file = new AlertDialog.Builder(HomePage.this);
                            save_file.setView(dialog_view);

                            final EditText filename = (EditText) dialog_view.findViewById(R.id.filename_editor);
                            save_file.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String content = editor.getText().toString();
                                    String state = Environment.getExternalStorageState();

                                    if (!(state.equals(Environment.MEDIA_MOUNTED))) {
                                        Toast.makeText(HomePage.this, "There is no any sd card", Toast.LENGTH_LONG).show();
                                    } else if (curr_lang == 0) {
                                        Toast.makeText(HomePage.this, "Select a language first!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else if (filename.getText().toString().equals("")) {
                                        Toast.makeText(HomePage.this, "Please Enter File Name", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } else {
                                        try {
                                            File myFile = new File("/sdcard/" + filename.getText().toString() + "." + extension[curr_lang]);
                                            myFile.createNewFile();

                                            prog_name.setText(filename.getText().toString() + "." + extension[curr_lang]);
                                            prog_name.setTextColor(Color.BLACK);
                                            prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                                            FileOutputStream fOut = new FileOutputStream(myFile);
                                            OutputStreamWriter myOutWriter =
                                                    new OutputStreamWriter(fOut);
                                            myOutWriter.append(editor.getText().toString());
                                            myOutWriter.close();
                                            fOut.close();
                                            Toast.makeText(getBaseContext(),
                                                    "File saved in sdcard/" + filename.getText().toString() + "." + extension[curr_lang],
                                                    Toast.LENGTH_SHORT).show();

                                            lang.setSelection(0);
                                            curr_lang = 0;
                                            prog_name.setText("**File Name");
                                            prog_name.setTextColor(Color.BLACK);
                                            prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                                            editor.setText("");
                                            Toast.makeText(HomePage.this, "New File Opened", Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            Toast.makeText(getBaseContext(), e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            save_file.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lang.setSelection(0);
                                    curr_lang = 0;
                                    prog_name.setText("**File Name");
                                    prog_name.setTextColor(Color.BLACK);
                                    prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                                    editor.setText("");
                                    Toast.makeText(HomePage.this, "New File Opened", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = save_file.create();
                            alert.show();


                        }

                    });//new_file yes close
                    new_file.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lang.setSelection(0);
                            curr_lang = 0;
                            prog_name.setText("**File Name");
                            prog_name.setTextColor(Color.BLACK);
                            prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                            editor.setText("");
                            Toast.makeText(HomePage.this, "New File Opened", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog al = new_file.create();
                    al.show();
                }
                if (position == 1) //Testcases
                {
                    //Toast.makeText(HomePage.this, "Testcases", Toast.LENGTH_SHORT).show();
                    LayoutInflater li = LayoutInflater.from(HomePage.this);
                    View dialog_view = li.inflate(R.layout.take_input, null);
                    AlertDialog.Builder input = new AlertDialog.Builder(HomePage.this);
                    input.setView(dialog_view);
                    final EditText input_test = (EditText) dialog_view.findViewById(R.id.editTextDialogUserInput);

                    input.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            // TODO Auto-generated method stub

                            if (curr_lang == 0) {
                                Toast.makeText(HomePage.this, "No language selected\nSelect a language first!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else if (editor.getText().toString().equals("")) {
                                Toast.makeText(HomePage.this, "No code inserted\nPlease insert code", Toast.LENGTH_SHORT).show();
                            } else {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                    dialog.dismiss();
                                    testcases = input_test.getText().toString();
                                    Intent intent = new Intent(HomePage.this, HackerRankAPIHandler.class);
                                    intent.putExtra("test_cases1", testcases);
                                    testcases = "";
                                    intent.putExtra("source_code1", editor.getText().toString());
                                    intent.putExtra("language_selected1", String.valueOf(language_codes[curr_lang]));
                                    startActivity(intent);
                                    Toast.makeText(HomePage.this, "Connected", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(HomePage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    input.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            testcases = "";
                        }
                    });
                    AlertDialog alert = input.create();
                    alert.show();
                }

                if (position == 0) //Run
                {
                    //Toast.makeText(HomePage.this, "Run", Toast.LENGTH_SHORT).show();
                    if (curr_lang == 0) {
                        Toast.makeText(HomePage.this, "No language selected\nSelect a language first!", Toast.LENGTH_SHORT).show();
                    } else if (editor.getText().toString().equals("")) {
                        Toast.makeText(HomePage.this, "No code inserted\nPlease insert code", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                testcases = "1\n";
                                Intent intent = new Intent(HomePage.this, HackerRankAPIHandler.class);
                                intent.putExtra("test_cases1", "1\n");
                                testcases = "";
                                intent.putExtra("source_code1", editor.getText().toString());
                                intent.putExtra("language_selected1", String.valueOf(language_codes[curr_lang]));
                                startActivity(intent);
                                Toast.makeText(HomePage.this, "Connected", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(HomePage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (position == 3)// Open
                {
                    //Toast.makeText(HomePage.this,"Select a laguage before opening a program\nIf already selected then ignore!",Toast.LENGTH_LONG).show();

                    LayoutInflater li = LayoutInflater.from(HomePage.this);
                    View dialog_view = li.inflate(R.layout.open_file, null);
                    AlertDialog.Builder file_save = new AlertDialog.Builder(HomePage.this);
                    file_save.setView(dialog_view);

                    final EditText filename = (EditText) dialog_view.findViewById(R.id.open_file_bro);

                    file_save.setPositiveButton("Open", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = editor.getText().toString();
                            String state = Environment.getExternalStorageState();

                            if (!(state.equals(Environment.MEDIA_MOUNTED))) {
                                Toast.makeText(HomePage.this, "There is no any sd card", Toast.LENGTH_LONG).show();
                            } else if (filename.getText().toString().equals("")) {
                                Toast.makeText(HomePage.this, "Please Enter File Name", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                try {

                                    File myFile = new File("/sdcard/" + filename.getText().toString());
                                    FileInputStream fIn = new FileInputStream(myFile);
                                    BufferedReader myReader = new BufferedReader(
                                            new InputStreamReader(fIn));
                                    String aDataRow = "";
                                    String aBuffer = "";
                                    while ((aDataRow = myReader.readLine()) != null) {
                                        aBuffer += aDataRow + "\n";
                                    }
                                    editor.setText(aBuffer);
                                    myReader.close();
                                    prog_name.setText(filename.getText().toString());
                                    int index = 0;
                                    String prog = prog_name.getText().toString();
                                    index = prog.indexOf(".");
                                    String lng = null;
                                    if (index > 0) {
                                        FILE_OPEN = 1;
                                        lng = prog.substring(index + 1, prog.length());
                                        lang.setSelection(hm.get(lng));
                                        curr_lang = hm.get(lng);
                                    }
                                    Toast.makeText(HomePage.this, "File Opened", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getBaseContext(), e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                    file_save.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = file_save.create();
                    alert.show();
                }
                if (position == 4) { // Save
                    if (prog_name.getText().toString().equals("**File Name") == false) {
                        try {
                            File myFile = new File("/sdcard/" + prog_name.getText().toString());
                            //myFile.createNewFile();
                            FileOutputStream fOut = new FileOutputStream(myFile);
                            OutputStreamWriter myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append(editor.getText().toString());
                            myOutWriter.close();
                            fOut.close();
                            Toast.makeText(getBaseContext(),
                                    "File saved in sdcard/" + prog_name.getText().toString(),
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        // Toast.makeText(HomePage.this,"Save",Toast.LENGTH_SHORT).show();
                        LayoutInflater li = LayoutInflater.from(HomePage.this);
                        View dialog_view = li.inflate(R.layout.save_file, null);
                        AlertDialog.Builder file_save = new AlertDialog.Builder(HomePage.this);
                        file_save.setView(dialog_view);

                        final EditText filename = (EditText) dialog_view.findViewById(R.id.filename_editor);
                        file_save.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String content = editor.getText().toString();
                                String state = Environment.getExternalStorageState();

                                if (!(state.equals(Environment.MEDIA_MOUNTED))) {
                                    Toast.makeText(HomePage.this, "There is no any sd card", Toast.LENGTH_LONG).show();
                                } else if (curr_lang == 0) {
                                    Toast.makeText(HomePage.this, "Select a language first!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (filename.getText().toString().equals("")) {
                                    Toast.makeText(HomePage.this, "Please Enter File Name", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    try {
                                        File myFile = new File("/sdcard/" + filename.getText().toString() + "." + extension[curr_lang]);
                                        myFile.createNewFile();

                                        prog_name.setText(filename.getText().toString() + "." + extension[curr_lang]);
                                        prog_name.setTextColor(Color.BLACK);
                                        prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                                        FileOutputStream fOut = new FileOutputStream(myFile);
                                        OutputStreamWriter myOutWriter =
                                                new OutputStreamWriter(fOut);
                                        myOutWriter.append(editor.getText().toString());
                                        myOutWriter.close();
                                        fOut.close();
                                        Toast.makeText(getBaseContext(),
                                                "File saved in sdcard/" + filename.getText().toString() + "." + extension[curr_lang],
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        file_save.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = file_save.create();
                        alert.show();
                    }
                }
                if(position == 5){//save as
                    LayoutInflater li = LayoutInflater.from(HomePage.this);
                    View dialog_view = li.inflate(R.layout.saveas, null);
                    AlertDialog.Builder file_save = new AlertDialog.Builder(HomePage.this);
                    file_save.setView(dialog_view);

                    final EditText filename = (EditText) dialog_view.findViewById(R.id.saveas_editor);
                    file_save.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = editor.getText().toString();
                            String state = Environment.getExternalStorageState();

                            if (!(state.equals(Environment.MEDIA_MOUNTED))) {
                                Toast.makeText(HomePage.this, "There is no any sd card", Toast.LENGTH_LONG).show();
                            } else if (curr_lang == 0) {
                                Toast.makeText(HomePage.this, "Select a language first!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else if (filename.getText().toString().equals("")) {
                                Toast.makeText(HomePage.this, "Please Enter File Name", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                try {
                                    File myFile1 = new File("/sdcard/" + prog_name.getText().toString());

                                    myFile1.delete();
                                    File myFile = new File("/sdcard/" + filename.getText().toString() + "." + extension[curr_lang]);
                                    myFile.createNewFile();
                                    prog_name.setText(filename.getText().toString() + "." + extension[curr_lang]);
                                    prog_name.setTextColor(Color.BLACK);
                                    prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
                                    FileOutputStream fOut = new FileOutputStream(myFile);
                                    OutputStreamWriter myOutWriter =
                                            new OutputStreamWriter(fOut);
                                    myOutWriter.append(editor.getText().toString());
                                    myOutWriter.close();
                                    fOut.close();
                                    Toast.makeText(getBaseContext(),
                                            "File saved in sdcard/" + filename.getText().toString() + "." + extension[curr_lang],
                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getBaseContext(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    file_save.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = file_save.create();
                    alert.show();

                }
                if (position == 6) {
                    //Toast.makeText(HomePage.this,"Contact Us",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomePage.this, Contact.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences SP = getSharedPreferences("data",0);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("edit", editor.getText().toString());
        ed.putInt("lang", curr_lang);
        ed.putString("progname", prog_name.getText().toString());
        ed.putInt("cur_open_lang", 1);
        ed.commit();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Tools!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            lang.setSelection(0);
            curr_lang = 0;
            prog_name.setText("**File Name");
            prog_name.setTextColor(Color.BLACK);
            prog_name.setTypeface(null, Typeface.BOLD_ITALIC);
            editor.setText("");
            return true;
        }
        if(id == R.id.help){
            Intent i = new Intent(HomePage.this,Contact.class);
            startActivity(i);
        }
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
