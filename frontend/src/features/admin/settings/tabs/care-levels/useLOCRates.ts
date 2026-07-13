import { useState, useEffect, useCallback } from 'react';
import toast from 'react-hot-toast';
import type { CareLevelRateResponse } from '../../../care-plans/types';
import { careLevelApi } from './care-level-api';
import { TIERS } from '../../../care-plans/utils/constants';

export const useLOCRates = () => {
  const [rates, setRates] = useState<CareLevelRateResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editData, setEditData] = useState({
    dailyRate: '',
    effectiveFrom: '',
  });

  const fetchRates = useCallback(async () => {
    try {
      setLoading(true);
      const allRates: CareLevelRateResponse[] = [];
      
      for (let i = 1; i <= 4; i++) {
        const response = await careLevelApi.getCareLevelRates(i);
        if (response.data && response.data.length > 0) {
          allRates.push(response.data[0]);
        } else {
          // Create default rate if none exists
          const createResponse = await careLevelApi.createCareLevelRate({
            careLevelId: i,
            facilityId: 1,
            dailyRate: parseFloat(TIERS[i - 1].defaultRate),
            effectiveFrom: new Date().toISOString().split('T')[0],
          });
          allRates.push(createResponse.data);
        }
      }
      
      setRates(allRates);
    } catch (error) {
      console.error('Error fetching rates:', error);
      toast.error('Failed to load LOC rates');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchRates();
  }, [fetchRates]);

  const handleEdit = (rate: CareLevelRateResponse) => {
    setEditingId(rate.id);
    setEditData({
      dailyRate: rate.dailyRate.toString(),
      effectiveFrom: rate.effectiveFrom,
    });
  };

  const handleCancel = () => {
    setEditingId(null);
    setEditData({ dailyRate: '', effectiveFrom: '' });
  };

  const handleSave = async (rateId: number) => {
    try {
      const updateData = {
        careLevelId: rates.find(r => r.id === rateId)?.careLevelId,
        dailyRate: parseFloat(editData.dailyRate),
        effectiveFrom: editData.effectiveFrom,
        effectiveTo: null,
      };

      await careLevelApi.updateCareLevelRate(rateId, updateData);
      
      setRates(rates.map(rate => 
        rate.id === rateId 
          ? { 
              ...rate, 
              dailyRate: parseFloat(editData.dailyRate),
              effectiveFrom: editData.effectiveFrom,
            }
          : rate
      ));
      
      setEditingId(null);
      toast.success('Rate updated successfully!');
    } catch (error) {
      console.error('Error updating rate:', error);
      toast.error('Failed to update rate');
    }
  };

  const handleSaveAll = async () => {
    try {
      // Check if there are any unsaved edits
      if (editingId) {
        await handleSave(editingId);
      }
      toast.success('✅ Đã lưu thay đổi thành công!');
    } catch (error) {
      toast.error('Failed to save changes');
    }
  };

  return {
    rates,
    loading,
    editingId,
    editData,
    TIERS,
    setEditData,
    handleEdit,
    handleSave,
    handleCancel,
    handleSaveAll,
    fetchRates,
  };
};
