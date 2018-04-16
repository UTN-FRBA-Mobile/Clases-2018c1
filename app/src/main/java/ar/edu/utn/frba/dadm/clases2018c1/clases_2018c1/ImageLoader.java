package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by emanuel on 24/4/17.
 */

public class ImageLoader {

    public final static ImageLoader instance = new ImageLoader();

    // Pool de 2 threads para bajar hasta 2 imágenes al mismo tiempo
    private Executor executor = Executors.newFixedThreadPool(2);
    // Handler para volver al main thread
    private Handler handler = new Handler(Looper.getMainLooper());
    // Mapa de imageViews asociados a la URL que tienen que cargar
    private Map<ImageView, URL> loadMap = new HashMap<>();
    // Caché simple de bitmaps asociados a URLs
    private Map<URL, Bitmap> bitmaps = new HashMap<>();
    // URLs descargando
    private Set<URL> loading = new HashSet<>();

    private ImageLoader() {
    }

    public void loadImage(final String urlString, final ImageView imageView) {
        try {
            loadImage(new URL(urlString), imageView);
        } catch (MalformedURLException ignored) {
            // la URL no es válida
        }
    }

    public void loadImage(final URL url, final ImageView imageView) {
        // Tenemos la URL y dónde hay que mostrarla
        loadMap.put(imageView, url);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (!loadMap.containsValue(url)) {
                    // Se canceló la descarga (la imageView necesita otra imagen)
                    return;
                }
                try {
                    final Bitmap bitmap;
                    if (bitmaps.containsKey(url)) {
                        // La imagen está en caché
                        bitmap = bitmaps.get(url);
                    }
                    else if (!loading.contains(url)) {
                        loading.add(url);
                        // Comenzamos la descarga
                        InputStream inputStream = url.openConnection().getInputStream();
                        // Decodificamos los datos
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        // Acá deberíamos redimensionar, para evitar usar imágenes muy grandes
                        bitmaps.put(url, bitmap);
                        loading.remove(url);
                    }
                    else {
                        // Está descargando, se resuelve en otro thread activo
                        return;
                    }
                    // Ya tenemos la imagen, buscamos todas las imageView que la solicitaron
                    final Set<Map.Entry<ImageView, URL>> entrySet = new HashSet<>(loadMap.entrySet());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (Map.Entry<ImageView, URL> entry : entrySet) {
                                if (url.equals(entry.getValue())) {
                                    // Ponemos la imagen en la imageView estando en el main thread
                                    entry.getKey().setImageBitmap(bitmap);
                                    loadMap.remove(entry.getKey());
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
