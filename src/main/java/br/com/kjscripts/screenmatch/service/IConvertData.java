package br.com.kjscripts.screenmatch.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> tClass);
}
