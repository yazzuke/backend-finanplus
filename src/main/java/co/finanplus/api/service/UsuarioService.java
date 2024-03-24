package co.finanplus.api.service;

import co.finanplus.api.domain.usuarios.Usuario;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private static final String COLLECTION_NAME = "Usuarios";

    public String saveUsuario(Usuario usuario) throws InterruptedException, ExecutionException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        try {
            // Crear usuario en Authentication
            CreateRequest request = new CreateRequest()
                    .setEmail(usuario.getEmail())
                    .setPassword(usuario.getPassword());
            UserRecord userRecord = firebaseAuth.createUser(request);

            // Asignar UID a la clase Usuario
            usuario.setUid(userRecord.getUid());

            // Crear documento en Firestore
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME)
                    .document(userRecord.getUid())
                    .set(usuario);

            return collectionApiFuture.get().getUpdateTime().toString();
        } catch (FirebaseAuthException e) {
            // Manejar excepciones de Authentication
            throw new RuntimeException("Error creando usuario en Firebase Authentication: " + e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            // Manejar excepciones del Future
            throw new RuntimeException("Error escribiendo en Firestore: " + e.getMessage());
        }
    }
}
