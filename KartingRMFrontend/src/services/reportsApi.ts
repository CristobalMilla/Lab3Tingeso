import { api } from './api'

export interface FeeTypeReportData {
  Descripcion: string
  [month: string]: string | number | undefined
  Total?: number
  'Gran Total'?: number
}

export const reportsApi = {
  getFeeTypeReport: async (startMonth: string, endMonth: string): Promise<FeeTypeReportData[]> => {
    const response = await api.get('/report/fee-type', {
      params: {
        startMonth,
        endMonth
      }
    })
    return response.data
  }
}