package se.ifmo.server.file.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.server.models.classes.Dragon;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Handles the serialization and deserialization of the dragon collection to and from XML files.
 * This class implements the {@link IOHandler} interface to provide functionality for reading and
 * writing the dragon collection as a {@link TreeMap} with {@link Long} keys and {@link Dragon} values.
 * Uses Jackson's {@link XmlMapper} for converting between XML and Java objects.
 */
public class XmlHandler implements IOHandler<TreeMap<Long, Dragon>> {
    /**
     * The path of the XML file where the dragon collection is stored.
     */
    private final Path filePath = Path.of("dragon_collection.xml");

    /**
     * The {@link XmlMapper} instance used for serializing and deserializing XML data.
     */
    private final XmlMapper xmlMapper = new XmlMapper();

    {
        xmlMapper.findAndRegisterModules();
    }

    /**
     * Reads the dragon collection from the XML file.
     * The file is expected to contain a list of {@link MapEntry} objects, which are converted into a
     * {@link TreeMap<Long, Dragon>}. If the file is not readable or an error occurs, an empty map is returned.
     *
     * @return the deserialized {@link TreeMap<Long, Dragon>} from the XML file.
     */
    @Override
    public TreeMap<Long, Dragon> read() {
        if (!Files.isReadable(filePath)) {
            System.err.println("File " + filePath.getFileName() + " is not readable");
            return new TreeMap<>();
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath.toFile()))) {
            List<MapEntry> entries = xmlMapper.readValue(inputStreamReader, new TypeReference<List<MapEntry>>() {});
            TreeMap<Long, Dragon> map = new TreeMap<>();
            entries.forEach(entry -> map.put(entry.getKey(), entry.getValue()));

            return map;
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath.getFileName());
            System.err.println(e.getMessage());
        }

        return new TreeMap<>();
    }

    /**
     * Writes the dragon collection to the XML file.
     * The dragon collection is serialized as a list of {@link MapEntry} objects and written to the file.
     * If the file is not writable or an error occurs, the operation is aborted.
     *
     * @param value the {@link TreeMap<Long, Dragon>} to be written to the file.
     */
    @Override
    public void write(TreeMap<Long, Dragon> value) {
        if (!Files.isWritable(filePath)) {
            System.err.println("File " + filePath.getFileName() + " is not writable");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            List<MapEntry> entries = new ArrayList<>();
            value.forEach((key, dragon) -> entries.add(new MapEntry(dragon.getId(), dragon)));
            xmlMapper.writer().withDefaultPrettyPrinter().writeValue(fileWriter, entries);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath.getFileName());
            System.err.println(e.getMessage());
        }
    }

    /**
     * Closes the resources used by the {@link XmlHandler}.
     * This method does nothing in this implementation as no specific resources need to be closed.
     */
    @Override
    public void close() {
    }
}
