package com.humane.util.file;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;

public class FileNameEncoder {
    public static String encode(String filename) {
        StringBuilder contentDisposition = new StringBuilder("attachment");
        CharsetEncoder enc = StandardCharsets.US_ASCII.newEncoder();
        boolean canEncode = enc.canEncode(filename);
        if (canEncode) {
            contentDisposition.append("; filename=").append('"').append(filename).append('"');
        } else {
            enc.onMalformedInput(CodingErrorAction.IGNORE);
            enc.onUnmappableCharacter(CodingErrorAction.IGNORE);

            String normalizedFilename = Normalizer.normalize(filename, Form.NFKD);
            CharBuffer cbuf = CharBuffer.wrap(normalizedFilename);

            ByteBuffer bbuf;
            try {
                bbuf = enc.encode(cbuf);
            } catch (CharacterCodingException e) {
                bbuf = ByteBuffer.allocate(0);
            }

            String encodedFilename = new String(bbuf.array(), bbuf.position(), bbuf.limit(), StandardCharsets.US_ASCII).trim();

            if (!encodedFilename.equals("")) {
                contentDisposition.append("; filename=").append('"').append(encodedFilename).append('"');
            }

            URI uri;
            try {
                uri = new URI(null, null, filename, null);
            } catch (URISyntaxException e) {
                uri = null;
            }

            if (uri != null) {
                contentDisposition.append("; filename*=UTF-8''").append(uri.toASCIIString());
            }
        }
        return contentDisposition.toString();
    }
}