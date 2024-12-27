package entities;

import java.io.Serializable;
import java.nio.file.Path;

public class Paylasilan_dosya implements Serializable
{
    private Integer paylasimci;
    private Integer alici;
    private Path paylasilan_dosya;

    public Integer getPaylasimci() {
        return paylasimci;
    }

    public void setPaylasimci(Integer paylasimci) {
        this.paylasimci = paylasimci;
    }

    public Integer getAlici() {
        return alici;
    }

    public void setAlici(Integer alici) {
        this.alici = alici;
    }

    public Path getPaylasilan_dosya() {
        return paylasilan_dosya;
    }

    public void setPaylasilan_dosya(Path paylasilan_dosya) {
        this.paylasilan_dosya = paylasilan_dosya;
    }
}
