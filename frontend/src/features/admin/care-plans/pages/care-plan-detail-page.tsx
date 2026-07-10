import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router'; 
import { ArrowLeft, Save } from 'lucide-react';
import GoalTab from '../tabs/goal-tab';
import InterventionTab from '../tabs/intervention-tab';

const CarePlanDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<'goals' | 'interventions'>('goals');

  return (
    <div className="max-w-6xl mx-auto">
      {/* Breadcrumb */}
      <div className="text-sm text-gray-500 mb-2">
        Admin &gt; Care Plans &gt; Detail
      </div>

      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate('/admin/care-plans')}
            className="text-gray-500 hover:text-gray-700"
          >
            <ArrowLeft className="w-6 h-6" />
          </button>
          <div>
            <h1 className="text-3xl font-semibold">Care Plan Detail</h1>
            <p className="text-gray-600">Plan ID: #{id}</p>
          </div>
        </div>
        <button className="px-6 py-2 bg-blue-600 text-white rounded-xl font-medium hover:bg-blue-700 transition flex items-center gap-2">
          <Save className="w-4 h-4" />
          Save Plan
        </button>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="flex gap-8">
          <button
            className={`py-3 px-1 border-b-2 font-medium transition ${
              activeTab === 'goals'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
            onClick={() => setActiveTab('goals')}
          >
            Goals
          </button>
          <button
            className={`py-3 px-1 border-b-2 font-medium transition ${
              activeTab === 'interventions'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
            onClick={() => setActiveTab('interventions')}
          >
            Interventions
          </button>
        </nav>
      </div>

      {/* Tab Content */}
      <div className="mt-6">
        {activeTab === 'goals' && <GoalTab carePlanId={parseInt(id || '0')} />}
        {activeTab === 'interventions' && <InterventionTab carePlanId={parseInt(id || '0')} />}
      </div>
    </div>
  );
};

export default CarePlanDetailPage;