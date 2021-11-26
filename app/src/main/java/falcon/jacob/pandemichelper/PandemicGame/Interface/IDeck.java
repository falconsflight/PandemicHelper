package falcon.jacob.pandemichelper.PandemicGame.Interface;
import java.util.Collection;

public interface IDeck<T> {
     public Collection<T> getCards();
     public void shuffle();
     public T draw() throws Exception;
     public void load();
     public Collection<T> deepCopy();
}