package com.project.sbjr.showinfodatabase.response;

import com.project.sbjr.showinfodatabase.model.Cast;
import com.project.sbjr.showinfodatabase.model.Crew;

import java.util.ArrayList;

/**
 * Created by sbjr on 28/12/16.
 */

public class CreditResponse {

    private ArrayList<Cast> cast;
    private ArrayList<Crew> crew;

    public CreditResponse() {
    }

    public CreditResponse(ArrayList<Cast> cast, ArrayList<Crew> crew) {
        this.cast = cast;
        this.crew = crew;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
    }
}
