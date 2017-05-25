package com.example.ktdie.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText eNombre, eID, eTelefono, eCorreo;
    String nombre, id, telefono, correo;
    private String FIREBASE_URL = "https://fir-6c1c4.firebaseio.com/";

    private FirebaseDatabase database;
    DatabaseReference myRef;

    int cont =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eNombre= (EditText)findViewById(R.id.eNombre);
        eID=(EditText) findViewById(R.id.eID);
        eTelefono=(EditText) findViewById(R.id.eTelefono);
        eCorreo=(EditText) findViewById(R.id.eCorreo);

        database = FirebaseDatabase.getInstance();
       // myRef = database.getReference("message");
    }

    public void  onClick (View view){
        int idb = view.getId();

        nombre = eNombre.getText().toString();
        id = eID.getText().toString();
        telefono = eTelefono.getText().toString();
        correo = eCorreo.getText().toString();


        switch (idb){
            case R.id.bGuardar:
                myRef = database.getReference();
                Contacto contacto = new Contacto(id, nombre, telefono, correo);
                myRef.child("contactos").child(String.valueOf(cont)).setValue(contacto);
                cont++;
                limpiar();
                break;

            case R.id.bActualizar:
                myRef =database.getReference("contactos").child(String.valueOf(id));
                //para la función updateChildren
                Map<String,Object> nuevonombre = new HashMap<>();
                nuevonombre.put("nombre",nombre);
                myRef.updateChildren(nuevonombre);

                Map<String,Object> nuevocorreo = new HashMap<>();
                nuevocorreo.put("correo",correo);
                myRef.updateChildren(nuevocorreo);

                Map<String,Object> nuevotelefono= new HashMap<>();
                nuevotelefono.put("telefono",telefono);
                myRef.updateChildren(nuevotelefono);
                limpiar();
                break;
            case R.id.bBorrar:
                myRef.child("contactos").child(String.valueOf(cont)).child(String.valueOf(id));
                myRef.removeValue();
                limpiar();
                break;
            case R.id.bBuscar:
                myRef =database.getReference("contactos");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(id).exists()){
                            Log.d("data",dataSnapshot.child(id).getValue().toString());
                            Contacto contacto = new Contacto();
                            contacto = dataSnapshot.child(id).getValue(Contacto.class);
                            eNombre.setText(contacto.getNombre());
                            eCorreo.setText(contacto.getCorreo());
                            eTelefono.setText(contacto.getTelefono());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                limpiar();
                break;

        }
    }

    private void limpiar() {
        eNombre.setText("");
        eID.setText("");
        eTelefono.setText("");
        eCorreo.setText("");
    }
}
