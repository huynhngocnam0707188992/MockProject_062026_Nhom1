import { useEffect, useState } from "react";
import { adminService, type PhiAccessLog, type PaginatedResponse } from "../../services/admin-service";

export const PhiAccessTab = () => {
  const [data, setData] = useState<PaginatedResponse<PhiAccessLog> | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await adminService.GetPhiAccessLogs({});
        setData(response);
      } catch (err) {
        console.error("Failed to load PHI logs", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="p-4 bg-white rounded shadow mt-4">
      <h3 className="text-lg font-semibold mb-4">PHI Access Logs</h3>
      {loading ? (
        <div>Loading...</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Table</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Record ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Access Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Reason</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Accessed By</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {data?.data.map((log) => (
                <tr key={log.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{log.id}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{log.table_name}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{log.record_id}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold">{log.access_type}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{log.access_reason}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{log.accessed_by}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{new Date(log.accessed_at).toLocaleString()}</td>
                </tr>
              ))}
              {(!data?.data || data.data.length === 0) && (
                <tr>
                  <td colSpan={7} className="px-6 py-4 text-center text-sm text-gray-500">No logs found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};
