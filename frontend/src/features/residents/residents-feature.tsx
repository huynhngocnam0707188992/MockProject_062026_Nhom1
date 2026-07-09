import { useState } from 'react'
import ResidentsList from './residents-list'
import ResidentDetail from './resident-detail'
import EditResident from './edit-resident'

export default function ResidentsFeature() {
  const [currentView, setCurrentView] = useState<'list' | 'detail' | 'edit'>('list')
  const [selectedResidentId, setSelectedResidentId] = useState<string>('')

  const handleViewDetail = (id: string) => {
    setSelectedResidentId(id)
    setCurrentView('detail')
  }

  const handleEdit = (id: string) => {
    setSelectedResidentId(id)
    setCurrentView('edit')
  }

  const handleBackToList = () => {
    setCurrentView('list')
  }

  if (currentView === 'detail') {
    return (
      <ResidentDetail 
        residentId={selectedResidentId} 
        onBack={handleBackToList} 
        onEdit={() => handleEdit(selectedResidentId)}
      />
    )
  }

  if (currentView === 'edit') {
    return (
      <EditResident
        residentId={selectedResidentId}
        onBack={handleBackToList}
      />
    )
  }

  return (
    <ResidentsList 
      onViewDetail={handleViewDetail} 
    />
  )
}
