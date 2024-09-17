package cms;
import java.util.List;

public interface Persistence<T> {

	void save(T entidade);

	void update(T entidade);

	List<T> list();

	boolean  remove(String id);


}