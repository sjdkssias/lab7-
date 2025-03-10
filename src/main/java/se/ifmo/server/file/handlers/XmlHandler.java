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

public class XmlHandler implements IOHandler<TreeMap<Long, Dragon>> {
    private final Path filePath = Path.of("dragon_collection.xml");
    private final XmlMapper xmlMapper = new XmlMapper();

    {
        xmlMapper.findAndRegisterModules();
    }


    @Override
    public TreeMap<Long, Dragon> read() {
        if (!Files.isReadable(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not readable");
            return new TreeMap<>();
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath.toFile()))) {
            XmlMapper mapper = new XmlMapper();

            List<MapEntry> entries = xmlMapper.readValue(inputStreamReader, new TypeReference<List<MapEntry>>() {});
            TreeMap<Long, Dragon> map = new TreeMap<>();
            entries.forEach(entry -> map.put(entry.getKey(), entry.getValue()));

            //if (dragons.isEmpty()) System.out.println("No vehicles found. Collection will be empty.");

            return map;
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath.getFileName());
            System.err.println(e.getMessage());
        }

        return new TreeMap<>();
    }

    @Override
    public void write(TreeMap<Long, Dragon> value) {
        if (!Files.isWritable(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not writable");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            List<MapEntry> entries = new ArrayList<>();
            value.forEach((key, dragon) -> entries.add(new MapEntry(dragon.getId(), dragon)));
            xmlMapper.writer().withDefaultPrettyPrinter().writeValue(fileWriter, entries);
            System.out.println(xmlMapper.writer().withDefaultPrettyPrinter().writeValueAsString(entries));
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath.getFileName());
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() {
    }
}