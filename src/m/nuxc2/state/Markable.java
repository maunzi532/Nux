package m.nuxc2.state;

import com.jme3.export.*;
import java.io.*;

public interface Markable extends Savable
{
	@Override
	default void write(JmeExporter ex) throws IOException{}

	@Override
	default void read(JmeImporter im) throws IOException{}
}