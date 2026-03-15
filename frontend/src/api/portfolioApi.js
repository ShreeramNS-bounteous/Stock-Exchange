import axiosClient from "./axios";

export const getPortfolio = () => {
  return axiosClient.get("/portfolio");
};