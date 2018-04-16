package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

public class Model {

    private int imageId;
    private int textId;

    public Model(int imageId, int textId) {
        this.imageId = imageId;
        this.textId = textId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getTextId() {
        return textId;
    }
}
