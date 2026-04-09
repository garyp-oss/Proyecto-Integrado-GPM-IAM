package com.gpm_iam.videojuego;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Catalogo {
 ArrayList<Videojuego> catalogo = new ArrayList<>();
 HashMap<String, List<Videojuego>> porGenero = new HashMap<>();

 public void catalogoCreate (){
    agregarJuego(new VideojuegoDigitales("Minecraft", 19.99 , "Microsoft Store"));
    catalogo.add(new VideojuegoDigitales("Final Fantasy VII Remake", 39.99 , "Steam"));
    catalogo.add(new VideojuegoDigitales("Pokemon Scarlet", 34.99 , "Emulador"));
    catalogo.add(new VideojuegoDigitales("Garry's mod", 14.99 , "Steam"));
    catalogo.add(new VideojuegoDigitales("The last of us Remastered", 29.99 , "Emulador"));
    catalogo.add(new VideojuegoDigitales("Red Dead Redemption 2", 59.99 , "Steam"));
    catalogo.add(new VideojuegoDigitales("Stardew valley", 9.99 , "Steam"));
    catalogo.add(new VideojuegoDigitales("GTA V", 19.99 , "Epic Store"));
    catalogo.add(new VideojuegoDigitales("Terraria", 7.99 , "Epic Store"));
    catalogo.add(new VideojuegoDigitales("Uncharted 4", 19.99 , "Microsoft Store"));
   } 

   // Este método añade al ArrayList Y al HashMap a la vez :)
   public void addJuego (Videojuego juego){
      catalogo.add(juego);
       porGenero.computeIfAbsent(juego.getGenero(), k -> new ArrayList<>()).add(juego);
   }

   // Con esto buscamos todos los juegos de un género
   public List<Videojuego> obtenerPorGenero(String genero) {
        return porGenero.getOrDefault(genero, new ArrayList<>());
    }
}
