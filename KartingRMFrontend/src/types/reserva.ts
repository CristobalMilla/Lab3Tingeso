// Types for the reservation system
export interface FeeType {
  feeTypeId: number
  lapNumber: number
  maxTime: number
  price: number
  duration: number
}

export interface RentEntity {
  rentId?: number
  rentCode?: string
  rentDate: string
  rentTime: string
  feeTypeId: number
  peopleNumber: number
  mainClient: string
  totalPrice?: number
}

export interface ReceiptEntity {
  receiptId?: number
  rentId?: number
  subClientName: string
  baseTariff?: number
  sizeDiscount?: number
  specialDiscount?: number
  aggregatedPrice?: number
  ivaPrice?: number
  finalPrice?: number
}

export interface RentRequest {
  rent: RentEntity
  subClients: string[]
}

export interface RentPreviewDTO {
  rent: RentEntity
  receipts: ReceiptEntity[]
}