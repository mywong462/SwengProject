package ch.epfl.sweng.swengproject;

public enum Categories {
    HELP, MEET, NEED, ALL
}

class CategoriesInfo{
    public static int size = 4;

    public static int convert(Categories c){
        switch(c){
            case HELP:
                return 0;

            case MEET:
                return 1;

            case NEED:
                return 2;

            default :
                return size -1;
        }
    }

}


