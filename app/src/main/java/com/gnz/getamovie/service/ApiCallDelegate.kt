package com.gnz.getamovie.service


interface NowPlayingDelegate : ApiCallDelegate

interface SearchMovieDelegate : ApiCallDelegate{
    var query: String
}