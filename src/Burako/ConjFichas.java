package Burako;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConjFichas {
    private List<Ficha> fichas;

    public ConjFichas() {
        fichas = new ArrayList<>();
        for (int j = 0; j < 2; j++){
            for (Color col : Color.values()){
                if(col.equals(Color.NINGUNO)){continue;}
                for (int num = 1; num <= 13; num++){
                    fichas.add(new Ficha(num,col));
                }
            }
            fichas.add(new Ficha(0, Color.NINGUNO));
        }
        mezclar();
    }

    public void mezclar(){
        Collections.shuffle(fichas);
    }

    public Ficha robarFicha(){
        if(fichas.isEmpty()){
            return null;
        }
        else{
            return fichas.remove(0);
        }
    }

    public boolean noMasFichas(){
        return fichas.isEmpty();
    }
}
