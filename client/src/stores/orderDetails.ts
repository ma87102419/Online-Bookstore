import { defineStore } from "pinia";
import type { OrderDetails } from "@/types";

const ORDER_DETAIL_STORAGE_KEY = "OrderDetails";

export const useOrderDetailsStore = defineStore("OrderDetails", {
  state: () => {
    let OrderDetails = {} as OrderDetails;
    const initOrderString: string | null = sessionStorage.getItem(
      ORDER_DETAIL_STORAGE_KEY
    );
    if (initOrderString !== null) {
      // cartString is a string
      const orderFromStorage = JSON.parse(initOrderString) as OrderDetails;
      OrderDetails = Object.assign({}, orderFromStorage);
    }
    return {
      OrderDetails: OrderDetails,
    };
  },
  actions: {
    clearOrderDetails() {
      sessionStorage.removeItem(ORDER_DETAIL_STORAGE_KEY);
      this.OrderDetails = {} as OrderDetails;
    },
    hasOrderDetails() {
      return sessionStorage.getItem(ORDER_DETAIL_STORAGE_KEY) != null;
    },
    setOrderDetails(OrderDetails: OrderDetails) {
      this.OrderDetails = OrderDetails;
      sessionStorage.setItem(
        ORDER_DETAIL_STORAGE_KEY,
        JSON.stringify(OrderDetails)
      );
    },
  },
});
