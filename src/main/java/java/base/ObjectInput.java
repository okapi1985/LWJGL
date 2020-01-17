package java.base;

import java.io.IOException;

public interface ObjectInput extends DataOutput {

	double readDouble() throws IOException;
	float readFloat() throws IOException;
	int readInt() throws IOException;

}
