package br.com.igti.android.ocorredor;

import java.util.Date;

/**
 * Created by maciel on 11/13/14.
 */
public class Corrida {
    private long mId;
    private Date mDataInicio;

    public Corrida() {
        mId = -1;
        mDataInicio = new Date();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getDataInicio() {
        return mDataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        mDataInicio = dataInicio;
    }

    public int getDurationSeconds(long endMillis) {
        return (int)((endMillis-mDataInicio.getTime()) / 1000);
    }

    public static String formataDuracao(int durationSeconds) {
        int segundos = durationSeconds % 60;
        int minutos = ((durationSeconds - segundos) / 60) % 60;
        int horas = (durationSeconds - (minutos * 60) - segundos) / 3600;
        return String.format("%02d:%02d:%02d",horas,minutos,segundos);
    }
}
