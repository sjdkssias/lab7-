package se.ifmo.server.file.handlers;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.server.models.classes.Dragon;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class XmlHandler implements IOHandler<TreeMap<Long, Dragon>>{
    private final File file = new File("dragon_collection.xml");
    private final XmlMapper xmlMapper = new XmlMapper();
    {
        xmlMapper.findAndRegisterModules();
    }
    @Override
    public TreeMap<Long, Dragon> read() {
        return null;
    }

    @Override
    public void write(TreeMap<Long, Dragon> value) throws IOException {
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(file, value);
    }

    @Override
    public void close(){

    }
}
