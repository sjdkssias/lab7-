package se.ifmo.server.file.handlers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import se.ifmo.server.models.classes.Dragon;

@Getter
@Setter
@JacksonXmlRootElement(localName = "entry")
public class MapEntry {
    @JacksonXmlProperty(localName = "key")
    private long key;

    @JacksonXmlProperty(localName = "value")
    private Dragon value;


    public MapEntry(long key, Dragon value) {
        this.key = key;
        this.value = value;
    }
}