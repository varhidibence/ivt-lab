package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private GT4500 mockShip;
  private TorpedoStore primMock;
  private TorpedoStore secMock;

  @BeforeEach
  public void init(){
    this.primMock = mock(TorpedoStore.class);
    this.secMock = mock(TorpedoStore.class);
    this.mockShip = new GT4500(primMock, secMock);
    this.ship = new GT4500();
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primMock.fire(1)).thenReturn(true);
    when(primMock.isEmpty()).thenReturn(false);

    // Act
    mockShip.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primMock).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(primMock.fire(1)).thenReturn(true);
    when(primMock.isEmpty()).thenReturn(false);
    when(secMock.fire(1)).thenReturn(true);
    when(secMock.isEmpty()).thenReturn(false);

    // Act
    mockShip.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primMock).fire(1);
    verify(secMock).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Primary_Store_Empty_Success(){
    // Arrange
    when(primMock.isEmpty()).thenReturn(true);
    when(secMock.isEmpty()).thenReturn(false);
    when(secMock.fire(1)).thenReturn(true);

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primMock, times(1)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock, times(0)).fire(1);
    verify(secMock, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Stores_Empty_Fail(){
    // Arrange
    when(primMock.isEmpty()).thenReturn(true);
    when(secMock.isEmpty()).thenReturn(true);
    when(primMock.fire(1)).thenReturn(false);
    when(secMock.fire(1)).thenReturn(false);

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primMock, times(1)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock, times(0)).fire(1);
    verify(secMock, times(0)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_Stores_NotEmpty_Success(){
    // Arrange
    when(primMock.isEmpty()).thenReturn(false);
    when(secMock.isEmpty()).thenReturn(false);
    when(primMock.fire(1)).thenReturn(true);
    when(secMock.fire(1)).thenReturn(true);

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primMock, times(1)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock).fire(1);
    verify(secMock).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Stores_NotEmpty_5times_Success(){
    // Arrange
    when(primMock.isEmpty()).thenReturn(false);
    when(secMock.isEmpty()).thenReturn(false);
    when(primMock.fire(1)).thenReturn(true);
    when(secMock.fire(1)).thenReturn(true);

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.ALL);
    result = mockShip.fireTorpedo(FiringMode.ALL);
    result = mockShip.fireTorpedo(FiringMode.ALL);
    result = mockShip.fireTorpedo(FiringMode.ALL);
    result = mockShip.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primMock, times(5)).isEmpty();
    verify(secMock, times(5)).isEmpty();
    verify(primMock, times(5)).fire(1);
    verify(secMock, times(5)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Primary_Empty_But_Secondary_success(){
    // Arrange
    when(primMock.isEmpty()).thenReturn(true);
    when(secMock.isEmpty()).thenReturn(false);
    when(primMock.fire(1)).thenReturn(false);
    when(secMock.fire(1)).thenReturn(true);

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primMock, times(1)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock, times(0)).fire(1);
    verify(secMock, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireLaser_Fail(){
    // Arrange

    // Act
    boolean result = mockShip.fireLaser(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_Secondary_NotEmpty_success(){
    // Arrange
    when(secMock.isEmpty()).thenReturn(false);
    when(primMock.isEmpty()).thenReturn(false);
    when(primMock.fire(1)).thenReturn(true);
    when(secMock.fire(1)).thenReturn(true);

    mockShip.fireTorpedo(FiringMode.SINGLE);
    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.SINGLE); //->wasPrimaryFiredLast = true

    // Assert
    verify(primMock, times(1)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock, times(1)).fire(1);
    verify(secMock, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_Secondary_Empty_success(){
    // Arrange
    when(secMock.isEmpty()).thenReturn(true);
    when(primMock.isEmpty()).thenReturn(false);
    when(primMock.fire(1)).thenReturn(true);

    mockShip.fireTorpedo(FiringMode.SINGLE); //->wasPrimaryFiredLast = true
    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primMock, times(2)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock, times(2)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_Both_Empty_fail(){
    // Arrange
    mockShip.fireTorpedo(FiringMode.SINGLE); //->wasPrimaryFiredLast = true
    when(secMock.isEmpty()).thenReturn(true);
    when(primMock.isEmpty()).thenReturn(true);
    when(primMock.fire(1)).thenReturn(true);

    
    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primMock, times(2)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    verify(primMock, times(1)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_Both_Empty__fail(){
    // Arrange
    when(secMock.isEmpty()).thenReturn(true);
    when(primMock.isEmpty()).thenReturn(true);

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primMock, times(1)).isEmpty();
    verify(secMock, times(1)).isEmpty();
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_FireMode_Null(){
    // Arrange

    // Act
    boolean result = mockShip.fireTorpedo(FiringMode.TEST);

    // Assert
    verify(primMock, times(0)).isEmpty();
    verify(secMock, times(0)).isEmpty();
    assertEquals(false, result);
  }

}
