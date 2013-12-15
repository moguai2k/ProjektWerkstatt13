package hm.edu.pulsebuddy.data.models;

import hm.edu.pulsebuddy.common.Gender;

import java.util.Calendar;
import java.util.Date;

import org.garret.perst.Persistent;

public class UserModel extends Persistent
{
  public static final int intIndex = 1;

  private String userName;
  private String password;

  private long birthday;

  private int weight;
  private int height;

  /* oO */
  private int activity;
  private int genre;

  private short gender;

  public UserModel()
  {
  }

  public UserModel( String userName )
  {
    this.userName = userName;
    
    this.height = 155;
    this.weight = 65;
    this.gender = 1;

    Calendar c = Calendar.getInstance();
    c.set( 1985, Calendar.JANUARY, 1 );
    this.birthday = c.getTimeInMillis();
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public Date getBirthday()
  {
    return new Date( this.birthday );
  }

  public void setBirthday( Date birthday )
  {
    this.birthday = birthday.getTime();
  }

  public int getWeight()
  {
    return weight;
  }

  public void setWeight( int weight )
  {
    this.weight = weight;
  }

  public int getHeight()
  {
    return height;
  }

  public void setHeight( int height )
  {
    this.height = height;
  }

  public int getActivity()
  {
    return activity;
  }

  public void setActivity( int activity )
  {
    this.activity = activity;
  }

  public int getGenre()
  {
    return genre;
  }

  public void setGenre( int genre )
  {
    this.genre = genre;
  }

  public Gender getGender()
  {
    return mapToGender( this.gender );
  }

  public void setGender( Gender gender )
  {
    this.gender = mapGender( gender );
  }

  private short mapGender( Gender gender )
  {
    switch ( gender )
    {
      case female:
        return 1;

      case male:
        return 2;

      default:
        return 0;
    }
  }

  private Gender mapToGender( short gender )
  {
    switch ( gender )
    {
      case 1:
        return Gender.female;

      case 2:
        return Gender.male;

      default:
        return null;
    }
  }

  @Override
  public String toString()
  {
    return "UserModel [userName=" + userName + ", password=" + password
        + ", birthday=" + birthday + ", weight=" + weight + ", height="
        + height + ", activity=" + activity + ", genre=" + genre + ", gender="
        + gender + "]";
  }

}
