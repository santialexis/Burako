package Burako;

public enum Color {
    Amarillo,Azul,Rojo, Negro,NINGUNO;

    public String emoji(){
        String e = "";
        switch (this){
            case Amarillo -> e = "🟨";
            case Azul -> e = "🟦";
            case Negro -> e = "⬛";
            case Rojo -> e = "🟥";
            case NINGUNO -> e = "🌟";
        }
        return e;
    }
}