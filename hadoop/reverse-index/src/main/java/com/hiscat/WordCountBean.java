package com.hiscat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author hiscat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WordCountBean implements WritableComparable<WordCountBean> {
    private String word;
    private String filename;
    private Integer count;

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s\t", word, filename, count);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(word);
        out.writeUTF(filename);
        out.writeInt(count);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        word = in.readUTF();
        filename = in.readUTF();
        count = in.readInt();
    }

    @Override
    public int compareTo(WordCountBean o) {
        final int i = this.word.compareTo(o.word);
        return i == 0 ? this.filename.compareTo(o.filename) : i;
    }
}
