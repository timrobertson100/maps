package org.gbif.maps;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Assert;
import org.junit.Test;

public class TileServerApplicationTest {

  @Test
  public void managedRestHighLevelClientClosesExtraResource() throws IOException {
    AtomicBoolean closeableClosed = new AtomicBoolean(false);
    Closeable closeable = () -> closeableClosed.set(true);

    TileServerApplication.TileServerSpringConfiguration.ManagedRestHighLevelClient client =
      new TileServerApplication.TileServerSpringConfiguration.ManagedRestHighLevelClient(
        RestClient.builder(new HttpHost("localhost", 9200, "http")));
    client.setExtraCloseable(closeable);
    client.close();

    Assert.assertTrue(closeableClosed.get());
  }
}
