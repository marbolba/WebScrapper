package scrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.PrintWriter;

public class WebScrapper
{
    int size=30;
    private String titles[];
    private String links[];
    private String artist;

    WebScrapper(String artistName)
    {
        titles=new String[size];
        links=new String[size];
        artist=artistName.toLowerCase();

        start();
    }
    void start()
    {
        getLinks(artist);

        for(int i=0;i<size;i++)
        {
            downloadLyrics(links[i],titles[i].toLowerCase());
        }
    }
    public void getLinks(String name)
    {
        Document doc;

        try{
            doc=Jsoup.connect("http://www.tekstowo.pl/piosenki_artysty,"+name+",popularne,malejaco,strona,1.html").get();

            Elements table=doc.select(".ranking-lista").select("a[href]");      //zwraca linki i jakies smieci..
            Elements title=doc.select(".ranking-lista");

            int i=0;
            for (Element link: table)
            {
                String url = link.attr("href");
                if(url.contains("/piosenka"))
                    links[i++]="http://www.tekstowo.pl"+url;
            }
            for(i=0;i<size;i++)
                titles[i]=title.select(".ranking-lista > div:nth-child("+(i+1)+") > a:nth-child(2)").text();

        }catch (Exception e)
        {
            System.out.println("BLAD danych");
        }
    }
    public void downloadLyrics(String link,String title)
    {
        Document doc;

        try{
            doc=Jsoup.connect(link).get();

            Elements table=doc.select(".song-text");
            String tekst=table.text();

            saveTextToFile(title,tekst.substring(16));
        }catch (Exception e)
        {
            System.out.println("BLAD danych");
        }
    }
    private void saveTextToFile(String name,String text)
    {
        String formatedName=artist;
        int index=name.indexOf("-")+2;
        formatedName+="-"+name.substring(index).replace(" ","_").replace(":","")+".txt";
        try{
            PrintWriter out = new PrintWriter("./text/"+formatedName);
            out.println(text);
            out.close();
        }catch (Exception e){}
        //System.out.println(formatedName);
    }
}